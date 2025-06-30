package com.example.pql.models;

import com.example.pkb.table.CallsTable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallsStar implements Condition {
    public final PqlObject var1;
    public final PqlObject var2;

    public CallsStar(PqlObject var1, PqlObject var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    @Override
    public String getName() {
        return "Calls*";
    }

    @Override
    public List<Statement> getCondition(PqlObject arg1, PqlObject arg2) {
        CallsTable callsTable = CallsTable.getInstance();
        List<Statement> result = new ArrayList<>();
        String targetProcedure = arg2.getName().replace("\"", "");

        // Znajdź wszystkie procedury, które (bezpośrednio lub pośrednio) wywołują targetProcedure
        Set<String> allCallers = findAllCallers(targetProcedure, callsTable);

        for (String caller : allCallers) {
            result.add(new Statement(caller));
        }

      //  System.out.println("DEBUG: Calls* found callers for " + targetProcedure + ": " + result);
        return result;
    }

    private Set<String> findAllCallers(String callee, CallsTable callsTable) {
        Set<String> allCallers = new HashSet<>();
        Set<String> visited = new HashSet<>();
        findCallersRecursive(callee, callsTable, allCallers, visited);
        return allCallers;
    }

    private void findCallersRecursive(String currentProc, CallsTable callsTable,
                                      Set<String> allCallers, Set<String> visited) {
        // Uniknij nieskończonej rekurencji dla cykli (chociaż SIMPLE nie pozwala na rekurencję)
        if (visited.contains(currentProc)) {
            return;
        }
        visited.add(currentProc);

        // Znajdź bezpośrednich callerów tej procedury
        for (String caller : callsTable.getAllCallers()) {
            if (callsTable.doesCall(caller, currentProc)) {
                if (allCallers.add(caller)) {
                    // Rekurencyjnie znajdź callerów tego callera
                    findCallersRecursive(caller, callsTable, allCallers, visited);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Calls*(" + var1 + ", " + var2 + ")";
    }
}