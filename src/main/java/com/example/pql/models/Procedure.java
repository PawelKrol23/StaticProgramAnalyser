package com.example.pql.models;

public class Procedure implements PqlObject {
    private final String name;

    public Procedure(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
