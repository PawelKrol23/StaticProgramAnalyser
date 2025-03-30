package com.example.pkb.ast;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AST {
    private static AST instance;
    public static AST getInstance() {
        if (instance == null) {
            instance = new AST();
        }
        return instance;
    }
}
