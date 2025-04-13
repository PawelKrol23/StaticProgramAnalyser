package com.example.pql.models;

import java.lang.reflect.Type;

public class CodeVariable implements PqlObject {
    private final String name;
    private final Type type;

    @Override
    public String getName() {
        if (type.equals(Integer.class))
            return name;
        //if (type.equals(String.class))
        else
            return "\"" + name + "\"";
    }

    public CodeVariable(String newName) {
        if (newName.matches("\\d+")) {
            this.name = newName;
            this.type = Integer.class;
        }
        else if (newName.contains("\"")) {
            this.name = newName.replace("\"", "");
            this.type = String.class;
        }
        else {
            this.name = newName;
            this.type = String.class;
        }
    }
}