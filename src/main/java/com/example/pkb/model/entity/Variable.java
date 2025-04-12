package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;
import com.example.pkb.model.attribute.VariableName;


public class Variable extends TNode {

    public Variable(String varName) {
        this.setAttr(new VariableName(varName));
        this.setType(EntityType.VARIABLE);
    }


    public Variable() {
        this.setAttr(new VariableName());
        this.setType(EntityType.VARIABLE);
    }
}
