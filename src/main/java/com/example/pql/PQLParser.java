package com.example.pql;

import com.example.pql.models.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PQLParser {
    private String variables;
    private String query;

    private HashMap<String, PqlObject> declaredVariables = new HashMap<>();
    private ArrayList<String> declaredVariablesNames = new ArrayList<>();

    private ArrayList<Condition> declaredConditions = new ArrayList<>();

    private final HashMap<String, String> typeClassMap = new HashMap<>() {{
       put("stmt", "com.example.pql.models.Statement");
       put("variable", "com.example.pql.models.Variable");
       put("codeVariable", "com.example.pql.models.CodeVariable");
    }};

    private final HashMap<String, String> conditionClassMap = new HashMap<>() {{
        put("modifies", "com.example.pql.models.Modifies");
        put("parent", "com.example.pql.models.Parent");
        put("follows", "com.example.pql.models.Follows");
    }};

    public void parsePQLs() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                variables = scanner.nextLine();
                query = scanner.nextLine();

                // remove whitespace characters, remove excessive spaces.
                variables = variables.replaceAll("[\r\n\t]", "");
                variables = variables.replaceAll("\\s+", " ");
                query = query.replaceAll("[\r\n\t]", "");
                query = query.replaceAll("\\s+", " ");

                parseVariables();

                for (PqlObject p : declaredVariables.values()) {
                    System.out.print("Loaded var named: ");
                    System.out.print(p.getName());
                    System.out.print(" of type: ");
                    System.out.println(p.getClass());
                }

                Query q = parseQuery();
                System.out.print("Query: ");
                System.out.println(q);

                System.out.print("Select Var: ");
                System.out.println(q.selectVar);

                System.out.print("Condition: ");
                System.out.println(q.condition);

                // Przykładowy wynik
                PQLEvaluator evaluator = new PQLEvaluator();
                evaluator.evaluateQuery(q);
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
            System.out.println("Class statement not found!");
            return new Statement("FATAL ERROR");
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
                        case "stmt":
                            type = "stmt";
                            break;
                        case "variable":
                            type = "variable";
                            break;
                        default:
                            if (splitted.matches("[a-zA-Z0-9]+")) {
                                if (declaredVariablesNames.contains(splitted)) {
                                    System.out.println("This name is already declared!!!");
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
            System.out.print("className:");
            System.out.println(className);
            System.out.println();
            conditionClass = Class.forName(className);
            Constructor<?> constructor = conditionClass.getConstructors()[0];
            Object conditionInstance = constructor.newInstance(variables);
            return (Condition) conditionInstance;
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            System.out.println("Condition statement not found!");
            return new ErrorCondition("FATAL ERROR");
        }
    }

    private Query parseQuery() {
        String[] parts = query.split(" such that ");
        String selectPart = parts[0].replace("Select ", "").trim();
        String conditionPart = parts[1].trim();

        String[] conditionParts = conditionPart.split("\\(");
        String conditionType = conditionParts[0].trim().toLowerCase();
        String conditionArgs = conditionParts[1].replace(")", "").trim();

        String[] args = conditionArgs.contains(", ") ? conditionArgs.split(", ") : conditionArgs.split(",");
        String var1String = args[0].trim();
        String var2String = args[1].trim();
        System.out.println("XD");
        System.out.println(var2String);
        System.out.println("XD");


        PqlObject var1 = declaredVariables.get(var1String);
        PqlObject var2;
        if (declaredVariables.containsKey(var2String)) {
            var2 = declaredVariables.get(var2String);
        }
        //else if (var2String.contains("\"") || var2String.isEmpty()) {
        else if (!var2String.isEmpty()) {
            // internal variable in SIMPLE code (e.g.: "Round", "x", "z", "Function1"
            System.out.println("var2String: ");
            System.out.println(var2String);

            var2 = createVariable(typeClassMap.get("codeVariable"), var2String);
        }
        else {
            System.out.println("NOT FOUND!!!");
            var2 = null;
        }

        Condition condition = createCondition(conditionClassMap.get(conditionType), var1, var2);
        declaredConditions.add(condition);


//        if (conditionType.equals("modifies"))
//            condition = new Modifies(var1, var2);
//        else
//            condition = new Modifies(var1, var2);
        return new Query(selectPart, condition);
    }
}
