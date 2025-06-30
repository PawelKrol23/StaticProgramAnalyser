package com.example.pql.models;

import com.example.pkb.table.ModifiesTable;
import java.util.List;

public class Modifies implements Condition {
    private final String className;
    public PqlObject var1;
    public PqlObject var2;

    public Modifies(PqlObject var1, PqlObject var2) {
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
        // Obsługa przypadku gdy pierwszy argument to numer linii (CodeVariable)
        if (arg1 instanceof CodeVariable) {
            int lineNumber = Integer.parseInt(arg1.getName());
            String variableName = arg2.getName();

            boolean modifies = ModifiesTable.getInstance()
                    .isVariableModifiedByStatement(lineNumber, variableName);

            // Zwracamy listę z jednym elementem jeśli warunek jest spełniony
            return modifies ? List.of(new Statement("1")) : List.of();
        }

        // Obsługa przypadku gdy drugi argument to nazwa zmiennej w cudzysłowach
        if (arg2 instanceof Variable) {
            String variableName = arg2.getName();

            return switch (arg1) {
                case Assign assign -> ModifiesTable.getInstance()
                        .getAssignsModifyingVariable(variableName)
                        .stream()
                        .map(lineNumber -> new Statement(lineNumber.toString()))
                        .toList();
                case Statement statement -> ModifiesTable.getInstance()
                        .getStatementsModifyingVariable(variableName)
                        .stream()
                        .map(lineNumber -> new Statement(lineNumber.toString()))
                        .toList();
                case While w -> ModifiesTable.getInstance()
                        .getWhilesModifyingVariable(variableName)
                        .stream()
                        .map(lineNumber -> new Statement(lineNumber.toString()))
                        .toList();
                case If i -> ModifiesTable.getInstance()
                        .getIfsModifyingVariable(variableName)
                        .stream()
                        .map(lineNumber -> new Statement(lineNumber.toString()))
                        .toList();
                case Procedure p -> ModifiesTable.getInstance()
                        .getProceduresModifyingVariable(variableName)
                        .stream()
                        .map(procName -> new Statement(procName))
                        .toList();
                default -> throw new IllegalStateException("Unexpected type: " + arg1.getClass().getName());
            };
        }

        throw new IllegalStateException("Unsupported Modifies query pattern");
    }
}