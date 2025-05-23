package com.example.frontend;

import com.example.pkb.ast.EntityType;

public record WrapperStatement(int codeLine, String procedureName, EntityType type) {
}