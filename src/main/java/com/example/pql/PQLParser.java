package com.example.pql;

import com.example.pql.models.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PQLParser {
    private String variables;
    private String query;

    private final HashMap<String, PqlObject> declaredVariables = new HashMap<>();
    private final ArrayList<String> declaredVariablesNames = new ArrayList<>();
    private final ArrayList<Condition> declaredConditions = new ArrayList<>();

    private final HashMap<String, String> typeClassMap = new HashMap<>() {{
        put("assign", "com.example.pql.models.Assign");
        put("if", "com.example.pql.models.If");
        put("stmt", "com.example.pql.models.Statement");
        put("while", "com.example.pql.models.While");
        put("variable", "com.example.pql.models.Variable");
        put("codeVariable", "com.example.pql.models.CodeVariable");
        put("procedure", "com.example.pql.models.Procedure");
    }};

    private final HashMap<String, String> conditionClassMap = new HashMap<>() {{
        put("modifies", "com.example.pql.models.Modifies");
        put("parent", "com.example.pql.models.Parent");
        put("follows", "com.example.pql.models.Follows");
        put("parent*", "com.example.pql.models.ParentFollow");
        put("calls", "com.example.pql.models.Calls");
        put("calls*", "com.example.pql.models.CallsStar");
        put("uses", "com.example.pql.models.Uses");
        put("follows*", "com.example.pql.models.FollowsStar");
    }};

    public void parsePQLs() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Handle BOOLEAN queries
                if (line.startsWith("Select BOOLEAN")) {
                    variables = "";
                    // Check if the query continues on the same line
                    if (line.contains("such that")) {
                        query = line;
                    } else {
                        if (!scanner.hasNextLine()) {
                            // Puste zapytanie BOOLEAN - zwracamy false
                            System.out.println("false");
                            continue;
                        }
                        query = line + " " + scanner.nextLine().trim();
                    }

                    try {
                        parseVariables();
                        Query q = parseQuery(true);
                        PQLEvaluator evaluator = new PQLEvaluator();
                        evaluator.evaluateQuery(q);
                    } catch (Exception e) {

                        System.out.println("false");
                    }

                    resetParserState();
                    continue;
                }

                // Normal queries
                variables = line;
                if (!scanner.hasNextLine()) {
                    break;
                }
                query = scanner.nextLine().trim();

                try {
                    parseVariables();
                    Query q = parseQuery(false);
                    PQLEvaluator evaluator = new PQLEvaluator();
                    evaluator.evaluateQuery(q);
                } catch (Exception e) {
                    System.out.println("none");
                }

                resetParserState();
            }
        }
    }

    private void resetParserState() {
        declaredVariables.clear();
        declaredConditions.clear();
        declaredVariablesNames.clear();
    }

    private PqlObject createVariable(String className, String name) {
        try {
            Class<?> variableClass = Class.forName(className);
            Constructor<?> constructor = variableClass.getConstructor(String.class);
            return (PqlObject) constructor.newInstance(name);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create variable: " + e.getMessage());
        }
    }

    private void saveVariables(String type, ArrayList<String> vars) {
        for (String variableName : vars) {
            if (declaredVariablesNames.contains(variableName)) {
                throw new RuntimeException("Duplicate variable: " + variableName);
            }
            declaredVariablesNames.add(variableName);
            declaredVariables.put(variableName, createVariable(typeClassMap.get(type), variableName));
        }
    }

    private void parseVariables() {
        String type = null;
        ArrayList<String> varsArray = new ArrayList<>();

        for (String variableDeclaration : variables.split(";")) {
            if (!variableDeclaration.trim().isEmpty()) {
                for (String token : variableDeclaration.trim().split("[,\\s]+")) {
                    if (token.isEmpty()) continue;

                    switch (token) {
                        case "if": type = "if"; break;
                        case "assign": type = "assign"; break;
                        case "stmt": type = "stmt"; break;
                        case "variable": type = "variable"; break;
                        case "while": type = "while"; break;
                        case "procedure": type = "procedure"; break;
                        default:
                            if (token.matches("[a-zA-Z0-9]+")) {
                                varsArray.add(token);
                            }
                    }
                }

                if (type != null && !varsArray.isEmpty()) {
                    saveVariables(type, varsArray);
                }
                type = null;
                varsArray = new ArrayList<>();
            }
        }
    }

    private Condition createCondition(String className, PqlObject var1, PqlObject var2) {
        try {
            Class<?> conditionClass = Class.forName(className);
            Constructor<?> constructor = conditionClass.getConstructor(PqlObject.class, PqlObject.class);
            return (Condition) constructor.newInstance(var1, var2);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create condition: " + e.getMessage());
        }
    }

    private Query parseQuery(boolean isBooleanQuery) {
        String[] parts = query.split(" such that ");
        String selectPart = parts[0].replace("Select ", "").trim();
        String conditionsPart = parts.length > 1 ? parts[1].trim() : "";

        List<Condition> conditions = new ArrayList<>();

        if (!conditionsPart.isEmpty()) {
            for (String conditionStr : conditionsPart.split(" and ")) {
                String[] conditionParts = conditionStr.split("\\(", 2);
                String conditionType = conditionParts[0].trim().toLowerCase();
                String conditionArgs = conditionParts[1].replace(")", "").trim();

                String[] args = conditionArgs.split("\\s*,\\s*");
                PqlObject var1 = parseArgument(args[0].trim(), false);
                PqlObject var2 = parseArgument(args[1].trim(), true);

                conditions.add(createCondition(conditionClassMap.get(conditionType), var1, var2));
            }
        }

        Query query = new Query(selectPart, conditions, isBooleanQuery);
        if (!isBooleanQuery && declaredVariables.containsKey(selectPart)) {
            query.setSelectedObject(declaredVariables.get(selectPart));
        }
        return query;
    }

    private PqlObject parseArgument(String arg, boolean isSecondArg) {
        if (arg.startsWith("\"") && arg.endsWith("\"")) {
            arg = arg.substring(1, arg.length() - 1);
            // If quoted, treat as a concrete procedure/variable name
            return createVariable(isSecondArg ? typeClassMap.get("procedure") :
                    typeClassMap.get("procedure"), arg);
        }
        else if (arg.matches("\\d+")) {
            return createVariable(typeClassMap.get("codeVariable"), arg);
        }
        else if (declaredVariables.containsKey(arg)) {
            return declaredVariables.get(arg);
        }
        else {
            // For undeclared variables in conditions
            return createVariable(isSecondArg ? typeClassMap.get("procedure") :
                    typeClassMap.get("procedure"), arg);
        }
    }
}