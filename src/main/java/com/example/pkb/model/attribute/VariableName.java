package com.example.pkb.model.attribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class VariableName implements Attribute {

    private String varName;

    public VariableName(String varName) {
        this.varName = varName;
    }
}
