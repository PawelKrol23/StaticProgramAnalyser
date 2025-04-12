package com.example.pkb.model.attribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ConstantValue implements Attribute {

    private int value;

    public ConstantValue(int value) {
        this.value = value;
    }
}
