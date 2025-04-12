package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;


public class Assign extends TNode {

    public Assign() {
        setType(EntityType.ASSIGN);
    }
}
