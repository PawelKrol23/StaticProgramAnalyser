package com.example.pkb.table;

import com.example.frontend.WrapperStatement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifiesTable {
    private static ModifiesTable instance;
    private final HashMap<String, Set<Integer>> variableModifiedByAssign = new HashMap<>();
    private final HashMap<String, Set<Integer>> variableModifiedByWhile = new HashMap<>();
    private final HashMap<String, Set<Integer>> variableModifiedByIf = new HashMap<>();
    private final HashMap<String, Set<String>> variableModifiedByProcedure = new HashMap<>();

    public static ModifiesTable getInstance() {
        if (instance == null) {
            instance = new ModifiesTable();
        }
        return instance;
    }

    public void addModifies(int lineNumber, String variableName, Stack<WrapperStatement> stack) {
        Set<Integer> assignModifies = variableModifiedByAssign.computeIfAbsent(variableName, k -> new TreeSet<>());
        assignModifies.add(lineNumber);

        stack.forEach(wrapperStatement -> {
            switch (wrapperStatement.type()) {
                case PROCEDURE -> {
                    Set<String> modifies = variableModifiedByProcedure.computeIfAbsent(variableName, k -> new TreeSet<>());
                    modifies.add(wrapperStatement.procedureName());
                }
                case IF -> {
                    Set<Integer> modifies = variableModifiedByIf.computeIfAbsent(variableName, k -> new TreeSet<>());
                    modifies.add(wrapperStatement.codeLine());
                }
                case WHILE -> {
                    Set<Integer> modifies = variableModifiedByWhile.computeIfAbsent(variableName, k -> new TreeSet<>());
                    modifies.add(wrapperStatement.codeLine());
                }
                default -> throw new IllegalStateException("Achievement get! How did we get there?");
            }
        });
    }

    public Set<Integer> getAssignsModifyingVariable(String variableName) {
        return variableModifiedByAssign.getOrDefault(variableName, Set.of());
    }

    public Set<String> getProceduresModifyingVariable(String variableName) {
        return variableModifiedByProcedure.getOrDefault(variableName, Set.of());
    }

    public Set<Integer> getWhilesModifyingVariable(String variableName) {
        return variableModifiedByWhile.getOrDefault(variableName, Set.of());
    }

    public Set<Integer> getIfsModifyingVariable(String variableName) {
        return variableModifiedByIf.getOrDefault(variableName, Set.of());
    }

    public Set<Integer> getStatementsModifyingVariable(String variableName) {
        Stream<Integer> assignsStream = variableModifiedByAssign.getOrDefault(variableName, Set.of()).stream();
        Stream<Integer> ifsStream = variableModifiedByIf.getOrDefault(variableName, Set.of()).stream();
        Stream<Integer> whileStream = variableModifiedByWhile.getOrDefault(variableName, Set.of()).stream();

        return Stream.concat(Stream.concat(assignsStream, ifsStream), whileStream)
                .collect(Collectors.toCollection(TreeSet::new));
    }
    public boolean isVariableModifiedByStatement(int lineNumber, String variableName) {
        // Sprawdź w assign
        if (variableModifiedByAssign.getOrDefault(variableName, Set.of()).contains(lineNumber)) {
            return true;
        }
        // Sprawdź w if
        if (variableModifiedByIf.getOrDefault(variableName, Set.of()).contains(lineNumber)) {
            return true;
        }
        // Sprawdź w while
        if (variableModifiedByWhile.getOrDefault(variableName, Set.of()).contains(lineNumber)) {
            return true;
        }
        return false;
    }
}
