package com.example.pql;

import com.example.pql.models.Condition;

class Query {
    String selectVar;
    Condition condition;

    public Query(String selectVar, Condition condition) {
        this.selectVar = selectVar;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Select " + selectVar + " such that " + condition;
    }
}