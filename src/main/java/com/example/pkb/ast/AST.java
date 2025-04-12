package com.example.pkb.ast;
import com.example.pkb.model.attribute.Attribute;
import com.example.pkb.model.entity.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AST {

    private static AST instance;

    private TNode root;


    public static AST getInstance() {
        if (instance == null) {
            instance = new AST();
        }
        return instance;
    }


    ///Setters: Operations to create an AST
    public TNode createTNode(EntityType et) {
        return switch (et) {
            case PROGRAM -> new Program();
            case PROCEDURE -> new Procedure();
            case STMTLIST -> new StatementList();
            case STMT -> new Statement();
            case ASSIGN -> new Assign();
            case CALL -> new Call();
            case WHILE -> new While();
            case IF -> new If();
            case PLUS -> new Plus();
            case MINUS -> new Minus();
            case TIMES -> new Times();
            case VARIABLE -> new Variable();
            case CONSTANT -> new Constant();
            case PROG_LINE -> new ProgLine();
        };
    }


    public void setRoot(TNode node) {
        root = node;
    }


    public void setAttr(TNode n, Attribute attr) {
        n.setAttr(attr);
    }


    public void setLink(LinkType relation, TNode n1, TNode n2) {
       switch (relation) {
           case FIRST_CHILD:
               n1.setFirstChild(n2);
               break;
           case RIGHT_SIBLING:
               n1.setRightSibling(n2);
               break;
           case PARENT:
               n1.setParent(n2);
               break;
       }
    }


    ///Getters: Operations to traverse an AST and retrieve information from it
    public TNode getRoot() {
        return root;
    }


    public EntityType getType(TNode node) {
        return node.getType();
    }


    public Attribute getAttr(TNode node) {
        return node.getAttr();
    }


    public TNode getLinkedNode(LinkType link, TNode node) {
        return switch (link) {
            case FIRST_CHILD -> node.getFirstChild();
            case RIGHT_SIBLING -> node.getRightSibling();
            case PARENT -> node.getParent();
        };
    }


    public boolean isLinked(LinkType link, TNode node1, TNode node2) {
        return switch (link) {
            case FIRST_CHILD -> node1.getFirstChild().equals(node2);
            case RIGHT_SIBLING -> node1.getRightSibling().equals(node2);
            case PARENT -> node1.getParent().equals(node2);
        };
    }

}
