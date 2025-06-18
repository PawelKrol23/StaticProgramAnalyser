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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable other = (Variable) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}