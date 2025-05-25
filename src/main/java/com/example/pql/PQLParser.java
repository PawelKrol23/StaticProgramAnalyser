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
    }};

    private final HashMap<String, String> conditionClassMap = new HashMap<>() {{
        put("modifies", "com.example.pql.models.Modifies");
        put("parent", "com.example.pql.models.Parent");
        put("follows", "com.example.pql.models.Follows");
        put("parent*", "com.example.pql.models.ParentFollow");
    }};

    public void parsePQLs() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                variables = scanner.nextLine();

                if (variables.toUpperCase().contains("BOOLEAN")) {
                    System.out.println("FALSE");
                    return;
                }
                query = scanner.nextLine();

                // remove whitespace characters, remove excessive spaces.
                variables = variables.replaceAll("[\r\n\t]", "");
                variables = variables.replaceAll("\\s+", " ");
                query = query.replaceAll("[\r\n\t]", "");
                query = query.replaceAll("\\s+", " ");

                //try {
                    parseVariables();
                //} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
//                    System.out.println("Error when parsing variables!");
//                }

                Query q;
                //try {
                    q = parseQuery();
//                } catch (Exception e) {
//                    System.out.println("Error when parsing Query!");
//                    continue;
//                }

//                for (PqlObject p : declaredVariables.values()) {
////                    System.out.print("Loaded var named: ");
////                    System.out.print(p.getName());
////                    System.out.print(" of type: ");
////                    System.out.println(p.getClass());
//                }

//                System.out.print("Query: ");
//                System.out.println(q);

//                System.out.print("Select Var: ");
//                System.out.println(q.selectVar);

//                System.out.print("Condition: ");
//                System.out.println(q.condition);

                // Przykładowy wynik
                PQLEvaluator evaluator = new PQLEvaluator();
                evaluator.evaluateQuery(q);

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
            System.out.println("FALSE");
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
//                    System.out.println("SPLITTED");
//                    System.out.println(splitted);
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
        String conditionPart = parts[1].trim();

        String[] conditionParts = conditionPart.split("\\(");
        String conditionType = conditionParts[0].trim().toLowerCase();
        String conditionArgs = conditionParts[1].replace(")", "").trim();

        String[] args = conditionArgs.contains(", ") ? conditionArgs.split(", ") : conditionArgs.split(",");
        String var1String = args[0].trim();
        String var2String = args[1].trim();
//        System.out.println(var2String);


        PqlObject var1 = declaredVariables.get(var1String);
        PqlObject var2;
        if (var2String.matches("\\d+")) {
            var2 = createVariable(typeClassMap.get("codeVariable"), var2String);
        }
        else if (declaredVariables.containsKey(var2String)) {
            var2 = declaredVariables.get(var2String);
        }
        //else if (var2String.contains("\"") || var2String.isEmpty()) {
        else if (!var2String.isEmpty()) {
            // internal variable in SIMPLE code (e.g.: "Round", "x", "z", "Function1"
//            System.out.println("var2String: ");
//            System.out.println(var2String);

            var2 = createVariable(typeClassMap.get("codeVariable"), var2String);
        }
        else {
//            System.out.println("NOT FOUND!!!");
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
