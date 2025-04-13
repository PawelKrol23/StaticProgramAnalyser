package com.example.pql.models;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ErrorCondition implements Condition {
//    public ErrorCondition(String message) {
//        this.message = message;
//    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public String message;

    @Override
    public String toString() {
        return this.getClass().getName() + ": [" + message + "]";
    }

    @Override
    public List<Statement> getCondition(Variable var){
        return new ArrayList<Statement>();
    }
}
