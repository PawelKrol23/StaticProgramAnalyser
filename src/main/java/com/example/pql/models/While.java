package com.example.pql.models;

public class While implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public While(String newName) {
        this.name = newName;
    }
    @Override
    public String toString() {return name;}
}
