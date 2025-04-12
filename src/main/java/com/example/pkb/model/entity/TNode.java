package com.example.pkb.model.entity;
import com.example.pkb.model.attribute.Attribute;
import com.example.pkb.ast.EntityType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class TNode {

    private EntityType type;

    private Attribute attr;

    private TNode firstChild;

    private TNode rightSibling;

    private TNode parent;

}
