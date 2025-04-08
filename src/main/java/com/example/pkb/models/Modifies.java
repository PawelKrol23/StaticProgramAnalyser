package com.example.pkb.models;

public class Modifies implements Condition {
    @Override
    public String getName() {
        return "Modifies";
    }

    protected String var1;
    protected String var2;

    public Modifies(String var1, String var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    @Override
    public String toString() {
        return  "Modifies (" + var1 + ", \"" + var2 + "\")";
    }

}
