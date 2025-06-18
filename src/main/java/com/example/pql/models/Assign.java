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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assign other = (Assign) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
