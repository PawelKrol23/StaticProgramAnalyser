package com.example.pql.models;


import com.example.pkb.table.ModifiesTable;
import java.util.List;

public class Modifies implements Condition {
    private final String className;
    public PqlObject var1;
    public PqlObject var2;

    public Modifies(PqlObject var1, PqlObject var2) {
        this.var1 = var1;
        this.var2 = var2;
        String[] tmp = this.getClass().getName().split("\\.");
        className = tmp[tmp.length - 1];
    }

    @Override
    public String getName() { return className; }

    @Override
    public String toString() {
        return className + " (" + var1.getName() + ", " + var2.getName() + ")";
    }

    @Override
    public List<Statement> getCondition(Variable var){
        return ModifiesTable.getInstance()
                .getAssignsModifyingVariable(var.getName())
                .stream()
                .map(lineNumber -> new Statement(lineNumber.toString()))
                .toList();
    }
}
