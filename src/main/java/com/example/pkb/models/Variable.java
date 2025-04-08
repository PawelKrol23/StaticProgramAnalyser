package com.example.pkb.models;

public class Variable implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public Variable(String newName) {
        this.name = newName;
    }
}