package com.example.pkb.model.attribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StatementNumber implements Attribute {

    private int statementNumber;

    public StatementNumber(int statementNumber) {
        this.statementNumber = statementNumber;
    }
}
