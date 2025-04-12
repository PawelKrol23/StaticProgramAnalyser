package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;
import com.example.pkb.model.attribute.ConstantValue;


public class Constant extends TNode {

    public Constant(int value) {
        this.setAttr(new ConstantValue(value));
        this.setType(EntityType.CONSTANT);
    }


    public Constant() {
        this.setAttr(new ConstantValue());
        this.setType(EntityType.CONSTANT);
    }
}
