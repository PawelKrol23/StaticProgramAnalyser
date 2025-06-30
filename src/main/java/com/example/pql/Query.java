package com.example.pql;

import com.example.pql.models.Condition;
import com.example.pql.models.PqlObject;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private final String selectVar;
    private final List<Condition> conditions;
    private final boolean isBooleanQuery;
    private PqlObject selectedObject;

    public Query(String selectVar, List<Condition> conditions, boolean isBooleanQuery) {
        this.selectVar = selectVar;
        this.conditions = conditions;
        this.isBooleanQuery = isBooleanQuery;
        this.selectedObject = null;
    }

    public Query(String selectVar, Condition condition, boolean isBooleanQuery) {
        this(selectVar, new ArrayList<>(), isBooleanQuery);
        this.conditions.add(condition);
    }

    // Konstruktor dla kompatybilności wstecznej
    public Query(String selectVar, List<Condition> conditions) {
        this(selectVar, conditions, false);
    }

    // Konstruktor dla kompatybilności wstecznej
    public Query(String selectVar, Condition condition) {
        this(selectVar, condition, false);
    }

    public String getSelectVar() {
        return selectVar;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public boolean isBooleanQuery() {
        return isBooleanQuery;
    }

    public PqlObject getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(PqlObject selectedObject) {
        this.selectedObject = selectedObject;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public String toString() {
        return "Query{" +
                "selectVar='" + selectVar + '\'' +
                ", conditions=" + conditions +
                ", isBooleanQuery=" + isBooleanQuery +
                ", selectedObject=" + selectedObject +
                '}';
    }
}