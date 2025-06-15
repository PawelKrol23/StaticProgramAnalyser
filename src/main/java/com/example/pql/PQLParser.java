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
        put("uses", "com.example.pql.models.Uses");
        put("parent", "com.example.pql.models.Parent");
    }};

    public void parsePQLs() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Pomiń puste linie
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Sprawdź czy to zapytanie BOOLEAN
                if (line.trim().startsWith("Select BOOLEAN")) {
                    System.out.println("false");
                    continue;
                }
                
                // Dla normalnych zapytań
                variables = line;
                if (!scanner.hasNextLine()) {
                    break;
                }
                query = scanner.nextLine();

                // remove whitespace characters, remove excessive spaces.
                variables = variables.replaceAll("[\r\n\t]", "");
                variables = variables.replaceAll("\\s+", " ");
                query = query.replaceAll("[\r\n\t]", "");
                query = query.replaceAll("\\s+", " ");

                try {
                    parseVariables();
                    Query q = parseQuery();
                    PQLEvaluator evaluator = new PQLEvaluator();
                    evaluator.evaluateQuery(q);
                } catch (Exception e) {
                    System.out.println("none");
                }

                declaredVariables.clear();
                declaredConditions.clear();
                declaredVariablesNames.clear();
            }
        }
    }

    private PqlObject createVariable(String className, String name) {
        Class<?> variableClass;
        try {
            variableClass = Class.forName(className);
            Constructor<?> constructor = variableClass.getConstructor(String.class);
            Object variableInstance = constructor.newInstance(name);
            return (PqlObject) variableInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Variable creation failed");
    }
}

    private void saveVariables(String type, ArrayList<String> vars) {
        for (String variableName : vars) {
            declaredVariablesNames.add(variableName);
            declaredVariables.put(variableName, createVariable(typeClassMap.get(type), variableName));
        }
    }

    private void parseVariables() {
        String type = null;
        ArrayList<String> varsArray = new ArrayList<>();

        for (String variableDeclaration : variables.split(";")) {
            if (!variableDeclaration.isEmpty()) {
                for (String splitted : variableDeclaration.split("[,\\s]")) {
                    switch (splitted) {
                        case " ":  // ignore whitespaces
                            break;
                        case "if":
                            type = "if";
                            break;
                        case "assign":
                            type = "assign";
                            break;
                        case "stmt":
                            type = "stmt";
                            break;
                        case "variable":
                            type = "variable";
                            break;
                        case "while":
                            type = "while";
                            break;
                        case "procedure":
                            type = "procedure";
                            break;

                        default:
                            if (splitted.matches("[a-zA-Z0-9]+")) {
                                if (declaredVariablesNames.contains(splitted)) {
//                                    System.out.println("This name is already declared!!!");
                                    return; //throw new Exception("Object already declared!");
                                }
                                varsArray.add(splitted);
                            }
                    }
                }
                // Save variables
                saveVariables(type, varsArray);
                type = null;
                varsArray = new ArrayList<>();
            }
        }
    }

    private Condition createCondition(String className, Object... variables) {
        Class<?> conditionClass;
        try {
////            System.out.print("className:");
//            System.out.println(className);
////            System.out.println();
////            System.out.println("variables:");
//            System.out.println(Arrays.toString(variables));
//            System.out.println();
            conditionClass = Class.forName(className);
            Constructor<?> constructor = conditionClass.getConstructors()[0];
            Object conditionInstance = constructor.newInstance(variables);
            return (Condition) conditionInstance;
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | InvocationTargetException | NullPointerException e) {
//            System.out.println("Condition statement not found!");
            return new ErrorCondition("FATAL ERROR");
        }
    }

    private Query parseQuery() {
        String[] parts = query.split(" such that ");
        String selectPart = parts[0].replace("Select ", "").trim();
        String conditionsPart = parts[1].trim();

        List<Condition> conditions = new ArrayList<>();

        // Rozdziel warunki po "and"
        String[] conditionsArray = conditionsPart.split(" and ");
        
        for (String conditionStr : conditionsArray) {
            String[] conditionParts = conditionStr.split("\\(");
            String conditionType = conditionParts[0].trim().toLowerCase();
            String conditionArgs = conditionParts[1].replace(")", "").trim();

            String[] args = conditionArgs.contains(", ") ? conditionArgs.split(", ") : conditionArgs.split(",");
            String var1String = args[0].trim();
            String var2String = args[1].trim();

            PqlObject var1 = declaredVariables.get(var1String);
            PqlObject var2;
            if (var2String.matches("\\d+")) {
                var2 = createVariable(typeClassMap.get("codeVariable"), var2String);
            } else if (declaredVariables.containsKey(var2String)) {
                var2 = declaredVariables.get(var2String);
            } else if (!var2String.isEmpty()) {
                var2 = createVariable(typeClassMap.get("codeVariable"), var2String);
            } else {
                var2 = null;
            }

            Condition condition = createCondition(conditionClassMap.get(conditionType), var1, var2);
            conditions.add(condition);
        }

        return new Query(selectPart, conditions);
    }
}