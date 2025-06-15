package com.example.pkb.table;

import com.example.frontend.WrapperStatement;
import com.example.pkb.ast.EntityType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsesTable {
    private static UsesTable instance;

    private final Map<Integer, Set<String>> stmtToVars = new HashMap<>();

    private final Map<String, Set<String>> procToVars = new HashMap<>();

    private final Map<String, Set<Integer>> varUsedByIf = new HashMap<>();

    private final Map<String, Set<Integer>> varUsedByWhile = new HashMap<>();

    public static UsesTable getInstance() {
        if (instance == null) instance = new UsesTable();
        return instance;
    }

    public void addUses(int stmt, String variable, Stack<WrapperStatement> context) {
        stmtToVars.computeIfAbsent(stmt, k -> new HashSet<>()).add(variable);

        for (WrapperStatement ws : context) {
            switch (ws.type()) {
                case PROCEDURE -> procToVars
                        .computeIfAbsent(ws.procedureName(), k -> new HashSet<>())
                        .add(variable);

                case IF -> varUsedByIf
                        .computeIfAbsent(variable, k -> new HashSet<>())
                        .add(ws.codeLine());

                case WHILE -> varUsedByWhile
                        .computeIfAbsent(variable, k -> new HashSet<>())
                        .add(ws.codeLine());

                default -> {}
            }
        }

    }

    // === Zapytania dla statement ===

    public boolean isUses(int stmt, String variable) {
        return stmtToVars.getOrDefault(stmt, Set.of()).contains(variable);
    }

    public Set<String> getUsedVariables(int stmt) {
        return stmtToVars.getOrDefault(stmt, Set.of());
    }

    public Set<Integer> getAllStatements() {
        return stmtToVars.keySet();
    }

    // === Zapytania dla procedure ===

    public boolean isUses(String procedure, String variable) {
        return procToVars.getOrDefault(procedure, Set.of()).contains(variable);
    }

    public Set<String> getUsedVariables(String procedure) {
        return procToVars.getOrDefault(procedure, Set.of());
    }

    public Set<String> getAllProcedures() {
        return procToVars.keySet();
    }

    // === Zapytania dla if/while ===

    public Set<Integer> getIfsUsing(String variable) {
        return varUsedByIf.getOrDefault(variable, Set.of());
    }

    public Set<Integer> getWhilesUsing(String variable) {
        return varUsedByWhile.getOrDefault(variable, Set.of());
    }


}
