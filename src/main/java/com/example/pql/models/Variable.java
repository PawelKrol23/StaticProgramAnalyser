package com.example.pql.models;

public class Variable implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public Variable(String newName) {
        // Usuwa wszystkie cudzysłowia ze Stringa
        this.name = newName.replaceAll("\"", "");
    }
}