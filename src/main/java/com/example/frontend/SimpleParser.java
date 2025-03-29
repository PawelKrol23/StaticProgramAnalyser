package com.example.frontend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleParser {
    private Tokenizer tokenizer;

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
        if (tokenizer.isNextToken("while")) {
            parseWhile();
        } else {
            parseAssign();
        }
    }

    private void parseAssign() {

    }

    private void parseWhile() {

    }
}
