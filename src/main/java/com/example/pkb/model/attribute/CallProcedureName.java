package com.example.pkb.model.attribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CallProcedureName implements Attribute {

    private String procName;

    public CallProcedureName(String procName) {
        this.procName = procName;
    }
}
