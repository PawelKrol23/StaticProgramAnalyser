package com.example.pkb.table;

import com.example.frontend.WrapperStatement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsesTable {
    private static UsesTable instance;
    private final HashMap<String, Set<Integer>> variableUsedByAssign = new HashMap<>();
    private final HashMap<String, Set<Integer>> variableUsedByWhile = new HashMap<>();
    private final HashMap<String, Set<Integer>> variableUsedByIf = new HashMap<>();
    private final HashMap<String, Set<String>> variableUsedByProcedure = new HashMap<>();

    public static UsesTable getInstance() {
        if (instance == null) instance = new UsesTable();
        return instance;
    }

    public void addUses(int lineNumber, String variableName, Stack<WrapperStatement> stack) {
        Set<Integer> assignUses = variableUsedByAssign.computeIfAbsent(variableName, k -> new TreeSet<>());
        assignUses.add(lineNumber);

        stack.forEach(wrapperStatement -> {
            switch (wrapperStatement.type()) {
                case PROCEDURE -> {
                    Set<String> uses = variableUsedByProcedure.computeIfAbsent(variableName, k -> new TreeSet<>());
                    uses.add(wrapperStatement.procedureName());
                }
                case IF -> {
                    Set<Integer> uses = variableUsedByIf.computeIfAbsent(variableName, k -> new TreeSet<>());
                    uses.add(wrapperStatement.codeLine());
                }
                case WHILE -> {
                    Set<Integer> uses = variableUsedByWhile.computeIfAbsent(variableName, k -> new TreeSet<>());
                    uses.add(wrapperStatement.codeLine());
                }
            }
        });
    }

    public Set<Integer> getStatementsUsingVariable(String variableName) {
        Stream<Integer> assignsStream = variableUsedByAssign.getOrDefault(variableName, Set.of()).stream();
        Stream<Integer> ifsStream = variableUsedByIf.getOrDefault(variableName, Set.of()).stream();
        Stream<Integer> whileStream = variableUsedByWhile.getOrDefault(variableName, Set.of()).stream();

        return Stream.concat(Stream.concat(assignsStream, ifsStream), whileStream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public boolean isUses(int stmt, String variable) {
        return getStatementsUsingVariable(variable).contains(stmt);
    }
    public Set<String> getVariablesUsedInStatement(int stmt) {
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Set<Integer>> entry : variableUsedByAssign.entrySet()) {
            if (entry.getValue().contains(stmt)) {
                result.add(entry.getKey());
            }
        }
        for (Map.Entry<String, Set<Integer>> entry : variableUsedByWhile.entrySet()) {
            if (entry.getValue().contains(stmt)) {
                result.add(entry.getKey());
            }
        }
        for (Map.Entry<String, Set<Integer>> entry : variableUsedByIf.entrySet()) {
            if (entry.getValue().contains(stmt)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public Set<Integer> getAllStatementsWithUses() {
        Set<Integer> result = new HashSet<>();
        variableUsedByAssign.values().forEach(result::addAll);
        variableUsedByWhile.values().forEach(result::addAll);
        variableUsedByIf.values().forEach(result::addAll);
        return result;
    }

}