package com.example.frontend;

import com.example.frontend.token.Tokenizer;
import com.example.pkb.ast.EntityType;
import com.example.pkb.table.CallsTable;
import com.example.pkb.table.FollowsTable;
import com.example.pkb.table.ModifiesTable;
import com.example.pkb.table.UsesTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SimpleParser {
    private Tokenizer tokenizer;
    private int lineCount = 0;
    private final Stack<WrapperStatement> wrapperStatementStack = new Stack<>();

    public void parseFile(String filePath) {
        tokenizer = createTokenizer(filePath);
        parseProgram();
    }

    private Tokenizer createTokenizer(String filePath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return new Tokenizer(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseProgram() {
        parseProcedure();
        while (tokenizer.hasNext()) {
            parseProcedure();
        }
    }

    private void parseProcedure() {
        tokenizer.matchToken("procedure");
        String procedureName = tokenizer.matchName();
        wrapperStatementStack.add(new WrapperStatement(-1, procedureName, EntityType.PROCEDURE));
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
        wrapperStatementStack.pop();
    }

    private void parseStatementList() {
        int previousStatementLine = -1;

        while (!tokenizer.isNextToken("}")) {
            int currentLine = lineCount + 1;
            parseStatement();

            if (previousStatementLine != -1) {
                FollowsTable.getInstance().addFollows(previousStatementLine, currentLine);
            }

            previousStatementLine = currentLine;
        }
    }

    private void parseStatement() {
        lineCount++;
        if (tokenizer.isNextToken("while")) {
            parseWhile();
        } else if(tokenizer.isNextToken("call")) {
            parseCall();
        } else if(tokenizer.isNextToken("if")) {
            parseIf();
        } else {
            parseAssign();
        }
    }

    private void parseAssign() {
        String variableOnLeft = tokenizer.matchName(); // np. x
        tokenizer.matchToken("=");

        List<String> usedVars = parseExpressionForUses();

        ModifiesTable.getInstance().addModifies(lineCount, variableOnLeft, wrapperStatementStack);
        for (String usedVar : usedVars) {
            UsesTable.getInstance().addUses(lineCount, usedVar, wrapperStatementStack);
        }

        tokenizer.matchToken(";");
    }

    private List<String> parseExpressionForUses() {
        List<String> usedVariables = new ArrayList<>();

        if (tokenizer.isNextToken("(")) tokenizer.matchToken("(");

        if (tokenizer.isNextInteger()) {
            tokenizer.matchInteger();
        } else {
            String var = tokenizer.matchName();
            usedVariables.add(var);
        }

        while (!tokenizer.isNextToken(";")) {
            tokenizer.matchOperator();

            if (tokenizer.isNextToken("(")) tokenizer.matchToken("(");

            if (tokenizer.isNextInteger()) {
                tokenizer.matchInteger();
            } else {
                String var = tokenizer.matchName();
                usedVariables.add(var);
            }

            if (tokenizer.isNextToken(")")) tokenizer.matchToken(")");
        }

        return usedVariables;
    }


    private void parseWhile() {
        wrapperStatementStack.add(new WrapperStatement(lineCount, null, EntityType.WHILE));
        tokenizer.matchToken("while");
        String variableName = tokenizer.matchName();

        UsesTable.getInstance().addUses(lineCount, variableName, wrapperStatementStack);

        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
        wrapperStatementStack.pop();
    }

    private void parseExpression() {
        if (tokenizer.isNextToken("(")) {
            tokenizer.matchToken("(");
        }
        if(tokenizer.isNextInteger()) {
            String constValue = tokenizer.matchInteger();
        } else {
            String variableName = tokenizer.matchName();
        }

        while (!tokenizer.isNextToken(";")) {
            String operator = tokenizer.matchOperator();
            if (tokenizer.isNextToken("(")) {
                tokenizer.matchToken("(");
            }
            if(tokenizer.isNextInteger()) {
                String constValue = tokenizer.matchInteger();
            } else {
                String variableName = tokenizer.matchName();
            }
            if (tokenizer.isNextToken(")")) {
                tokenizer.matchToken(")");
            }
        }
    }

    private void parseCall() {
        tokenizer.matchToken("call");
        String callee = tokenizer.matchName();

        String caller = wrapperStatementStack.peek().procedureName();

        CallsTable.getInstance().addCalls(caller, callee);
        CallsTable.getInstance().addCallStatement(lineCount, callee);

        tokenizer.matchToken(";");
    }

    private void parseIf() {
        wrapperStatementStack.add(new WrapperStatement(lineCount, null, EntityType.IF));
        tokenizer.matchToken("if");
        String variableName = tokenizer.matchName();

        UsesTable.getInstance().addUses(lineCount, variableName, wrapperStatementStack);

        tokenizer.matchToken("then");
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
        tokenizer.matchToken("else");
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
        wrapperStatementStack.pop();
    }
}
