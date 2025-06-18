package com.example.pql.models;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement other = (Statement) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
