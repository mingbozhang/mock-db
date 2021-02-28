package com.customdb.keyword;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mingbozhang
 */

public enum EnumSortDirection {

    ASC("asc"),
    DESC("desc");

    private static Map<String, EnumSortDirection> sortDirectionMap = new HashMap<>();

    static {
        sortDirectionMap.put(ASC.direction, ASC);
        sortDirectionMap.put(DESC.direction, DESC);
    }


    private String direction;

    EnumSortDirection(String direction) {
        this.direction = direction;
    }

    public static EnumSortDirection getByString(String direction) {
        if (!sortDirectionMap.containsKey(direction)) {
            throw new IllegalArgumentException("输入不合法: " + direction);
        }
        return sortDirectionMap.get(direction);
    }
}
