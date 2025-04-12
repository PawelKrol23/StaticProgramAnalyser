package com.example.pkb.model.entity;
import com.example.pkb.ast.EntityType;
import com.example.pkb.model.attribute.StatementNumber;


public class Statement extends TNode {

    public Statement(int statementNumber) {
        this.setAttr(new StatementNumber(statementNumber));
        this.setType(EntityType.STMT);
    }


    public Statement() {
        this.setAttr(new StatementNumber());
        this.setType(EntityType.STMT);
    }
}
