package com.customdb.keyword;

import lombok.Getter;

import java.util.function.BiPredicate;

/**
 * @author mingbozhang
 */

public enum EnumComparisonOperator {

    /**
     * 小于
     */
    LT("<", (left, right) -> left.compareTo(right) < 0),

    /**
     * 小于或者等于
     */
    LE("<=", (left, right) -> left.compareTo(right) <= 0),

    /**
     * 大于
     */
    GT(">", (left, right) -> left.compareTo(right) > 0),

    /**
     * 大于或者等于
     */
    GE(">=", (left, right) -> left.compareTo(right) >= 0),

    /**
     * 等于
     */
    EQ("=", (left, right) -> left.compareTo(right) == 0);

    @Getter
    private String symbol;
    private BiPredicate comparisonFunction;

    EnumComparisonOperator(String symbol, BiPredicate<Comparable, Comparable> comparisonFunction) {
        this.symbol = symbol;
        this.comparisonFunction = comparisonFunction;
    }

    public boolean compute(Comparable left, Comparable right) {
        return comparisonFunction.test(left, right);
    }

}
