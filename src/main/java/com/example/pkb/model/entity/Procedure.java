package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;
import com.example.pkb.model.attribute.ProcedureName;


public class Procedure extends TNode {

    public Procedure(String procName) {
        this.setAttr(new ProcedureName(procName));
        this.setType(EntityType.PROCEDURE);
    }


    public Procedure() {
        this.setAttr(new ProcedureName());
        this.setType(EntityType.PROCEDURE);
    }
}
