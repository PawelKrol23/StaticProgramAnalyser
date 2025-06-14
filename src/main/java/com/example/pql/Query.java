package com.example.pql;

import com.example.pql.models.Condition;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private final String selectVar;
    private final List<Condition> conditions;

    public Query(String selectVar, List<Condition> conditions) {
        this.selectVar = selectVar;
        this.conditions = conditions;
    }

    public Query(String selectVar, Condition condition) {
        this.selectVar = selectVar;
        this.conditions = new ArrayList<>();
        this.conditions.add(condition);
    }

    public String getSelectVar() {
        return selectVar;
    }

    public List<Condition> getConditions() {
        return conditions;
    }
}