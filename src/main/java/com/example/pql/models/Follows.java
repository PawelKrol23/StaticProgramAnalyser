package com.example.pql.models;


import com.example.pkb.table.FollowsTable;

import java.util.ArrayList;
import java.util.List;

public class Follows implements Condition {
    private final String className;
    public PqlObject var1;
    public PqlObject var2;

    public Follows(PqlObject var1, PqlObject var2) {
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
    public List<Statement> getCondition(PqlObject arg1, PqlObject arg2) {
        FollowsTable followsTable = FollowsTable.getInstance();
        List<Statement> result = new ArrayList<>();

        boolean isArg1Number = arg1.getName().matches("\\d+");
        boolean isArg2Number = arg2.getName().matches("\\d+");

        if (isArg1Number && isArg2Number) {
            int s1 = Integer.parseInt(arg1.getName());
            int s2 = Integer.parseInt(arg2.getName());
            if (followsTable.isFollows(s1, s2)) {
                result.add(new Statement(String.valueOf(s1)));
            }
        } else if (isArg1Number) {
            int s1 = Integer.parseInt(arg1.getName());
            Integer s2 = followsTable.getFollows(s1);
            if (s2 != null) {
                result.add(new Statement(String.valueOf(s1)));
            }
        } else if (isArg2Number) {
            int s2 = Integer.parseInt(arg2.getName());
            Integer s1 = followsTable.getFollowedBy(s2);
            if (s1 != null) {
                result.add(new Statement(String.valueOf(s1)));
            }
        } else {
            // Oba są symboliczne — zwróć wszystkie Follows jako pary (lewy element jako wynik)
            for (Integer s1 : followsTable.getAllFollowKeys()) {
                Integer s2 = followsTable.getFollows(s1);
                if (s2 != null) {
                    result.add(new Statement(String.valueOf(s1)));
                }
            }
        }

        return result;
    }
}
