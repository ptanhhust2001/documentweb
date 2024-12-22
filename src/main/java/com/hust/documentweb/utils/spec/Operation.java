package com.hust.documentweb.utils.spec;

import lombok.Data;

@Data
public class Operation {
    enum OPERATOR {
        LIKE,
        EQUAL,
        LARGER,
        SMALLER
    }

    private String key;
    private String value;
    private String op;

    public Operation(String key, String op, String value) {
        this.key = key;
        this.op = op;
        this.value = value;
    }
}
