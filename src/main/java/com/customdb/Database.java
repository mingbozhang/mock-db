package com.customdb;

import com.customdb.keyword.GroupBy;
import com.customdb.keyword.Limit;
import com.customdb.keyword.OrderBy;
import com.customdb.keyword.Where;

import java.util.List;
import java.util.Optional;

/**
 * @author mingbozhang
 */
public class Database {

    /**
     * 查询
     *
     * @param data
     * @param where
     * @param orderBy
     * @param groupBy
     * @param limit
     * @return
     */
    public Object query(List<Object> data, Where where, OrderBy orderBy, GroupBy groupBy, Limit limit) {
        // 检查输入
        Class dataType = checkInput(data);

        return data.parallelStream()
                .filter(where.buildPredicateByDataType(dataType))
                .sorted(orderBy.buildComparator(dataType))
                .limit(limit.getSize())
                .collect(groupBy.buildGroupByCollector(dataType));
    }

    private Class checkInput(List<Object> data) {
        //检查是不是所有数据都是一个类型的
        return checkIfSameType(data);
    }

    private Class checkIfSameType(List<Object> data) {
        Optional<List<Object>> dataOpt = Optional.of(data);
        List<Object> list = dataOpt.get();

        if (list.size() <= 1) {
            return Object.class;
        }

        Class dataClass = list.get(0).getClass();
        if (list.subList(1, list.size() - 1).stream().allMatch((element) -> dataClass.isInstance(element))) {
            return dataClass;
        }

        throw new IllegalArgumentException("数据类型不统一");
    }

}
