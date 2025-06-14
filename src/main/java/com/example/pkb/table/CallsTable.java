package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CallsTable {
    private static CallsTable instance;

    private final Map<String, Set<String>> callsMap = new HashMap<>();
    private final Map<Integer, String> callStatements = new HashMap<>();

    public static CallsTable getInstance() {
        if (instance == null) instance = new CallsTable();
        return instance;
    }

    public void addCalls(String caller, String callee) {
        callsMap.computeIfAbsent(caller, k -> new HashSet<>()).add(callee);
    }

    public boolean doesCall(String caller, String callee) {
        return callsMap.getOrDefault(caller, Set.of()).contains(callee);
    }

    public void addCallStatement(int lineNumber, String procedureName) {
        callStatements.put(lineNumber, procedureName);
    }

    public Set<String> getAllCallers() {
        return callsMap.keySet();
    }

    public String getCalledProcedure(int lineNumber) {
        return callStatements.get(lineNumber);
    }

    public Set<Integer> getAllCallLines() {
        return callStatements.keySet();
    }
}