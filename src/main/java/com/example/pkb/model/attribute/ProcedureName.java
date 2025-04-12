package com.example.pkb.model.attribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProcedureName implements Attribute {

    private String procName;

    public ProcedureName(String procName) {
        this.procName = procName;
    }
}
