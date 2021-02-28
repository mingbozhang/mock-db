package com.customdb.keyword;

import com.customdb.util.ReflectionUtil;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author mingbozhang
 */
@Builder
public class GroupBy {

    private String groupString;

    public GroupBy(String groupString) {
        this.groupString = groupString;
    }

    public Collector buildGroupByCollector(Class dataType) {
        if (StringUtils.isBlank(groupString)) {
            return Collectors.toList();
        }

        Collector groupingByCollector = null;
        String[] fieldNames = groupString.split(",");

        for (String fieldName : fieldNames) {
            Field field = ReflectionUtil.getDeclaredField(dataType, fieldName);

            if (groupingByCollector == null) {
                groupingByCollector = Collectors.groupingBy(o -> {
                    try {
                        return field.get(o);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                });
                continue;
            }

            groupingByCollector = Collectors.groupingBy(o -> {
                try {
                    return field.get(o);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }, groupingByCollector);
        }

        return groupingByCollector;
    }

}
