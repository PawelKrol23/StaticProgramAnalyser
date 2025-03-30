package com.example.pkb.variable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VarTable {
    private static VarTable instance;
    private final List<String> variables = new ArrayList<>();

    public static VarTable getInstance() {
        if (instance == null) {
            instance = new VarTable();
        }
        return instance;
    }

    public int insertVar(String varName) {
        int index = variables.indexOf(varName);
        if (index == -1) {
            variables.add(varName);
            return variables.size() - 1;
        }

        return index;
    }

    public String getVarName(int index) {
        return variables.get(index);
    }

    public int getVarIndex(String varName) {
        return variables.indexOf(varName);
    }

    public int getSize() {
        return variables.size();
    }

    public boolean isIn(String varName) {
        return variables.contains(varName);
    }
}
