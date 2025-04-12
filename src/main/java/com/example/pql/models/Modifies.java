package com.example.pql.models;

import java.util.ArrayList;
import java.util.List;

public class Modifies implements Condition {
    @Override
    public String getName() {
        return "Modifies";
    }

    public String var1;
    protected String var2;

    public Modifies(String var1, String var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    @Override
    public String toString() {
        return  "Modifies (" + var1 + ", \"" + var2 + "\")";
    }


    public List<Statement> getModifies(Variable var){
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement("TESTPQL"));
        return statements;
    }
}
