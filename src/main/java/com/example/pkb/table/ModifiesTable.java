package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifiesTable {
    private static ModifiesTable instance;
    private final HashMap<String, Set<Integer>> variableModifiedByAssign = new HashMap<>();

    public static ModifiesTable getInstance() {
        if (instance == null) {
            instance = new ModifiesTable();
        }
        return instance;
    }

    public void addModifies(int lineNumber, String variableName) {
        Set<Integer> modifies = variableModifiedByAssign.computeIfAbsent(variableName, k -> new TreeSet<>());
        modifies.add(lineNumber);
    }

    public Set<Integer> getAssignsModifyingVariable(String variableName) {
        return variableModifiedByAssign.getOrDefault(variableName, Set.of());
    }
}
