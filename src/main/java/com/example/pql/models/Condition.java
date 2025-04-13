package com.example.pql.models;

import java.util.List;

public interface Condition {
    String getName();

    List<Statement> getCondition(Variable var);
}
