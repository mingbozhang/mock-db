package com.customdb;

import com.customdb.keyword.GroupBy;
import com.customdb.keyword.Limit;
import com.customdb.keyword.OrderBy;
import com.customdb.keyword.Where;
import com.customdb.model.Student;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author mingbozhang
 */
public class Database {

    public static void main(String[] args) {

        List<Object> students = new ArrayList<>();
        students.add(new Student(1, "Bob", 18, LocalDate.of(2003, 1, 1)));
        students.add(new Student(2, "Penny", 18, LocalDate.of(2003, 2, 2)));
        students.add(new Student(1, "John", 20, LocalDate.of(2001, 1, 1)));
        students.add(new Student(2, "Cassie", 23, LocalDate.of(1999, 1, 1)));
        students.add(new Student(2, "Cassie", 1, LocalDate.of(1999, 1, 1)));

//        Where where = Where.builder().criteriaString(" age=18").build();
        Where where = Where.builder().build();
        OrderBy orderBy = OrderBy.builder().orderString("name asc,age desc").build();
//        GroupBy groupBy = new GroupBy("age,sex");
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(Long.MAX_VALUE).build();

        Database database = new Database();
        Object result = database.query(students, where, orderBy, groupBy, limit);
        System.out.printf(new Gson().toJson(result));
    }

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

        return data.stream()
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
