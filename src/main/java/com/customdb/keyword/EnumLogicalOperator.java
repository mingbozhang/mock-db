package com.customdb.keyword;

import lombok.Getter;

/**
 * @author mingbozhang
 */

public enum EnumLogicalOperator {
    AND("and"),
    OR("or");

    @Getter
    private String symbol;

    EnumLogicalOperator(String symbol) {
        this.symbol = symbol;
    }
}
