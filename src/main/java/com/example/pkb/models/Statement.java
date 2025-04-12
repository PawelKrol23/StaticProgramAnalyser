package com.example.pkb.models;

public class Statement implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public Statement(String newName) {
        this.name = newName;
    }
    @Override
    public String toString() {return name;}
}
