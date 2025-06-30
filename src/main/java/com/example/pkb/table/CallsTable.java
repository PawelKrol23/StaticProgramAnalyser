package com.example.pkb.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

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
      // System.out.println("DEBUG: addCalls - caller: '" + caller + "' calls callee: '" + callee + "'");
    }

    public boolean doesCall(String caller, String callee) {
        boolean result = callsMap.getOrDefault(caller, Collections.emptySet()).contains(callee);
        //System.out.println("DEBUG: doesCall? caller: '" + caller + "', callee: '" + callee + "' -> " + result);
        return result;
    }

    public void addCallStatement(int lineNumber, String procedureName) {
        callStatements.put(lineNumber, procedureName);
       // System.out.println("DEBUG: addCallStatement - line: " + lineNumber + ", procedure: '" + procedureName + "'");
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
    public Set<String> getAllProcedures() {
        Set<String> allProcs = new HashSet<>();
        allProcs.addAll(callsMap.keySet());
        callsMap.values().forEach(allProcs::addAll);
        return allProcs;
    }

    public Set<String> getDirectCallers(String callee) {
        Set<String> callers = new HashSet<>();
        for (String caller : callsMap.keySet()) {
            if (callsMap.get(caller).contains(callee)) {
                callers.add(caller);
            }
        }
        return callers;
    }
    // Dodatkowa metoda pomocnicza do debugowania zawartości callsMap
    public void printCallsMap() {
        System.out.println("DEBUG: Current callsMap content:");
        for (Map.Entry<String, Set<String>> entry : callsMap.entrySet()) {
            System.out.println("  Caller: '" + entry.getKey() + "' calls -> " + entry.getValue());
        }
    }
}
