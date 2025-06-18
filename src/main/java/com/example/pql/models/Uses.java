package com.example.pql.models;

import com.example.pkb.table.UsesTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Uses implements Condition {
    private final String className;
    public PqlObject var1;
    public PqlObject var2;

    public Uses(PqlObject var1, PqlObject var2) {
        this.var1 = var1;
        this.var2 = var2;
        String[] tmp = this.getClass().getName().split("\\.");
        className = tmp[tmp.length - 1];
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public String toString() {
        return className + "(" + var1.getName() + ", " + var2.getName() + ")";
    }

    @Override
    public List<Statement> getCondition(PqlObject arg1, PqlObject arg2) {
        UsesTable usesTable = UsesTable.getInstance();
        List<Statement> result = new ArrayList<>();

        boolean isArg1Number = arg1.getName().matches("\\d+");
        boolean isArg2Name = arg2.getName().matches("[a-zA-Z][a-zA-Z0-9]*");

        if (isArg1Number && isArg2Name) {
            int stmt = Integer.parseInt(arg1.getName());
            if (usesTable.isUses(stmt, arg2.getName())) {
                result.add(new Statement(String.valueOf(stmt)));
            }
        } else if (isArg1Number) {
            int stmt = Integer.parseInt(arg1.getName());
            Set<String> variables = usesTable.getVariablesUsedInStatement(stmt);
            if (!variables.isEmpty()) {
                result.add(new Statement(String.valueOf(stmt)));
            }
        } else if (isArg2Name) {
            Set<Integer> statements = usesTable.getStatementsUsingVariable(arg2.getName());
            for (Integer stmt : statements) {
                result.add(new Statement(String.valueOf(stmt)));
            }
        } else {
            Set<Integer> allStatements = usesTable.getAllStatementsWithUses();
            for (Integer stmt : allStatements) {
                result.add(new Statement(String.valueOf(stmt)));
            }
        }

        return result;
    }
}