package com.example.pql.models;

import com.example.pkb.table.CallsTable;
import com.example.pql.models.Procedure;

import java.util.ArrayList;
import java.util.List;

public class Calls implements Condition {
    public final PqlObject var1;
    public final PqlObject var2;

    public Calls(PqlObject var1, PqlObject var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    @Override
    public String getName() {
        return "Calls";
    }

    @Override
    public List<Statement> getCondition(PqlObject arg1, PqlObject arg2) {
        CallsTable callsTable = CallsTable.getInstance();
        List<Statement> result = new ArrayList<>();

        String proc1 = arg1.getName();
        String proc2 = arg2.getName().replace("\"", "");

        for (String caller : callsTable.getAllCallers()) {
            if (callsTable.doesCall(caller, proc2)) {
                result.add(new Statement(caller));
            }
        }

        return result;
    }



    @Override
    public String toString() {
        return "Calls(" + var1 + ", " + var2 + ")";
    }
}
