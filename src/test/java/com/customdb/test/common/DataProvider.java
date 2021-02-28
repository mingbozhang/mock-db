package com.customdb.test.common;

import com.customdb.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供测试数据
 *
 * @author mingbozhang
 */
public final class DataProvider {
    private DataProvider() {
    }

    public static List<Object> getStudents() {
        List<Object> students = new ArrayList<>();
        students.add(new Student(1, "Bob", 18, LocalDate.of(2003, 1, 1)));
        students.add(new Student(2, "Penny", 18, LocalDate.of(2003, 2, 2)));
        students.add(new Student(1, "John", 20, LocalDate.of(2001, 1, 1)));
        students.add(new Student(2, "Cassie", 23, LocalDate.of(1999, 1, 1)));
        students.add(new Student(2, "Cassie", 1, LocalDate.of(1999, 1, 1)));
        return students;
    }
}
