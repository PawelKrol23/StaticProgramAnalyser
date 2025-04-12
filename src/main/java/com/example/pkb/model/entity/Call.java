package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;
import com.example.pkb.model.attribute.CallProcedureName;


public class Call extends TNode {

    public Call(String procName) {
       setAttr(new CallProcedureName(procName));
       setType(EntityType.CALL);
    }


    public Call() {
        setAttr(new CallProcedureName());
        setType(EntityType.CALL);
    }
}
