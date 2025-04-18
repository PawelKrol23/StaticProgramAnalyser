package com.example.pql.models;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Parent implements Condition {
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public String var1;
    protected String var2;

    @Override
    public String toString() {
        return this.getClass().getName() + " (" + var1 + ", \"" + var2 + "\")";
    }

    @Override
    public List<Statement> getCondition(Variable var){
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement("Parent"));
        return statements;
    }
}
