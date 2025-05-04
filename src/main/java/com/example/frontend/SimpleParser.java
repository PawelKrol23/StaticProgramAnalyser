package com.example.frontend;

import com.example.frontend.token.Tokenizer;
import com.example.pkb.table.ModifiesTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleParser {
    private Tokenizer tokenizer;
    private int lineCount = 0;

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
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
    }

    private void parseStatementList() {
        parseStatement();
        while (!tokenizer.isNextToken("}")) {
            parseStatement();
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
        String variableOnLeft = tokenizer.matchName();
        ModifiesTable.getInstance().addModifies(lineCount, variableOnLeft);
        tokenizer.matchToken("=");
        parseExpression();
        tokenizer.matchToken(";");
    }

    private void parseWhile() {
        tokenizer.matchToken("while");
        String variableName = tokenizer.matchName();
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
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
        String procedureName = tokenizer.matchName();
        tokenizer.matchToken(";");
    }

    private void parseIf() {
        tokenizer.matchToken("if");
        String variableName = tokenizer.matchName();
        tokenizer.matchToken("then");
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
        tokenizer.matchToken("else");
        tokenizer.matchToken("{");
        parseStatementList();
        tokenizer.matchToken("}");
    }
}
