package com.example.pql;

import com.example.pkb.models.Condition;
import com.example.pkb.models.Modifies;
import com.example.pkb.models.PqlObject;
import com.example.pkb.models.Statement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class PQLParser {
    private String variables;
    private String query;

    private ArrayList<PqlObject> declaredVariables = new ArrayList<>();
    private ArrayList<String> declaredVariablesNames = new ArrayList<>();

    private HashMap<String, String> typeClassMap = new HashMap<>() {{
       put("stmt", "com.example.pkb.models.Statement");
       put("variable", "com.example.pkb.models.Variable");
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

                for (PqlObject p : declaredVariables) {
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
                System.out.println("2,4");
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
            declaredVariables.add(createVariable(typeClassMap.get(type), variableName));
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

    private HashMap<String, String> keywordMap = new HashMap<>() {{
        put("select", "select");
        put("such that", "such that");
        put("modifies", "modifies");
    }};
    private String currentKeyword;

    private void processQueryPart(String part) {
        System.out.print("processQueryPart: ");
        System.out.println(part);
        switch (currentKeyword) {
            case "select":
                System.out.println("parsing select");
                break;
            case "such that":
                System.out.println("parsing such that");
                break;
            case "modifies":
                System.out.println("parsing modifies");
                break;
            default:
                if (keywordMap.containsKey(part)) {
                    currentKeyword = part;
                    System.out.print("keyword change to: ");
                    System.out.println(currentKeyword);
                }
                break;
        }
    }

    private Query parseQuery() {
        //String[] workingQuery = query.toLowerCase().split("(?=such that|select| )");

        String[] parts = query.split(" such that ");
        String selectPart = parts[0].replace("Select ", "").trim();
        String conditionPart = parts[1].trim();

        String[] conditionParts = conditionPart.split("\\(");
        String conditionType = conditionParts[0].trim();
        String conditionArgs = conditionParts[1].replace(")", "").trim();

        String[] args = conditionArgs.split(", ");
        String var1 = args[0].trim();  // "s"
        String var2 = args[1].replace("\"", "").trim();

        Condition condition;
        if (conditionType.equals("Modifies"))
            condition = new Modifies(var1, var2);
        else
            condition = new Modifies(var1, var2);
        return new Query(selectPart, condition);

//        if (!Objects.equals(workingQuery[0].toLowerCase(), "select")) {
//            System.out.println("Query must start with select!!!");
//            return;
//        }
//        currentKeyword = "select";
//
//        //Select s such that Modifies (s, "z")
//
//        for (int i=1; i < workingQuery.length; i++) {
//            processQueryPart(workingQuery[i]);
//        }
    }
}
