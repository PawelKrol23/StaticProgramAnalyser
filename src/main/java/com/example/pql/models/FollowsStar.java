package com.example.pql.models;

import com.example.pkb.table.FollowsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FollowsStar implements Condition {
    private final String className;
    public PqlObject var1;
    public PqlObject var2;
    public FollowsStar(PqlObject var1, PqlObject var2) {
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
    public List<Statement> getCondition(PqlObject var1, PqlObject var2) {
        FollowsTable followsTable = FollowsTable.getInstance();
        List<Statement> result = new ArrayList<>();

        // Obsługa różnych kombinacji argumentów
        if (var1 instanceof CodeVariable && var2 instanceof CodeVariable) {
            // Follows*(1, 2)
            int stmt1 = Integer.parseInt(var1.getName());
            int stmt2 = Integer.parseInt(var2.getName());
            if (followsTable.isFollowsStar(stmt1, stmt2)) {
                result.add(new Statement(String.valueOf(stmt1)));
            }
        }
        else if (var1 instanceof CodeVariable && var2 instanceof Variable) {
            // Follows*(1, s2)
            int stmt1 = Integer.parseInt(var1.getName());
            Set<Integer> following = followsTable.getAllFollowing(stmt1);
            following.forEach(stmt -> result.add(new Statement(String.valueOf(stmt))));
        }
        else if (var1 instanceof Variable && var2 instanceof CodeVariable) {
            // Follows*(s1, 2)
            int stmt2 = Integer.parseInt(var2.getName());
            Set<Integer> followers = followsTable.getAllFollowedByStar(stmt2);
            followers.forEach(stmt -> result.add(new Statement(String.valueOf(stmt))));
        }
        else if (var1 instanceof Variable && var2 instanceof Variable) {
            // Follows*(s1, s2) - wszystkie pary spełniające relację
            followsTable.getAllFollowsStarRelationships().forEach((s1, followers) -> {
                followers.forEach(s2 -> {
                    result.add(new Statement(s1 + "," + s2));
                });
            });
        }

        return result;
    }
}