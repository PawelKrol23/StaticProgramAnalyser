package com.example.pql.models;

public class Assign implements PqlObject {
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public Assign(String newName) {
        this.name = newName;
    }
    @Override
    public String toString() {return name;}
}
