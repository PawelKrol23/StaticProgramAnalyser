package com.example.pql.models;

public class If implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public If(String newName) {
        this.name = newName;
    }
    @Override
    public String toString() {return name;}
}
