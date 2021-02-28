package com.customdb.keyword;

import com.customdb.util.ReflectionUtil;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * @author mingbozhang
 */
@Builder
public class OrderBy {

    private String orderString;

    public OrderBy(String orderString) {
        this.orderString = orderString;
    }

    public Comparator buildComparator(Class dataType) {


        Comparator comparator = (left, right) -> 0;
        if (StringUtils.isBlank(orderString)) {
            return comparator;
        }

        //field1 asc,field2 desc
        String[] fieldNameAndSortArr = orderString.split(",");

        for (String fieldNameAndSort : fieldNameAndSortArr) {

            //0：field,1:asc/desc
            String[] elements = fieldNameAndSort.split(" ");

            Field field = ReflectionUtil.getDeclaredField(dataType, elements[0]);
            EnumSortDirection sortDirection = EnumSortDirection.getByString(elements[1]);

            comparator = comparator.thenComparing((left, right) -> {
                try {

                    int comparsionResult = getComparable(field, left).compareTo(getComparable(field, right));
                    if (sortDirection.equals(EnumSortDirection.DESC)) {
                        comparsionResult *= -1;
                    }

                    return comparsionResult;
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
        return comparator;
    }

    private Comparable getComparable(Field field, Object o) throws IllegalAccessException {
        return (Comparable) field.get(o);
    }
}
