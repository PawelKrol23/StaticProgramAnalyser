package com.example.pql;

import com.example.pkb.models.PqlObject;
import com.example.pkb.models.Statement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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

                parseQuery();

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

    private void parseQuery() {
        String[] queryArray;
        for (String v : query.split(" ")) {
            if (!v.isEmpty()) {
                System.out.println(v);
                // TODO: parse query or smth?
            }
        }
    }
}
