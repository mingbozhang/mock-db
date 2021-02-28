package com.customdb.test;

import com.customdb.Database;
import com.customdb.keyword.GroupBy;
import com.customdb.keyword.Limit;
import com.customdb.keyword.OrderBy;
import com.customdb.keyword.Where;
import com.customdb.model.Student;
import com.customdb.test.common.DataProvider;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mingbozhang
 */
public class DatabaseTest {

    private Database database;

    @Before
    public void before() {
        database = new Database();
    }

    @After
    public void after() {

    }

    /**
     * 查询没有过滤条件的情况
     */
    @Test
    public void testQuery001() {
        Where where = Where.builder().build();
        OrderBy orderBy = OrderBy.builder().build();
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(Long.MAX_VALUE).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(ArrayList.class.isInstance(result));

        List<Object> resultList = (ArrayList) result;
        Assert.assertEquals(inputDataList.size(), resultList.size());

    }

    /**
     * 查询只有Where条件，且只有and的情况
     */
    @Test
    public void testQuery002() {
        Where where = Where.builder().criteriaString("age>=20 and sex=1 and name=John").build();
        OrderBy orderBy = OrderBy.builder().build();
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(Long.MAX_VALUE).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(ArrayList.class.isInstance(result));

        List<Object> resultList = (ArrayList) result;
        Assert.assertEquals(1, resultList.size());
        Assert.assertEquals("John", ((Student) resultList.get(0)).getName());
    }

    /**
     * 查询只有Where条件，且只有and 和 or的情况
     */
    @Test
    public void testQuery003() {
        Where where = Where.builder().criteriaString("age>=20 and sex=1 or sex=2").build();
        OrderBy orderBy = OrderBy.builder().build();
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(Long.MAX_VALUE).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(ArrayList.class.isInstance(result));

        List<Student> resultList = (ArrayList<Student>) result;
        Assert.assertEquals(4, resultList.size());

        Assert.assertEquals(3, resultList.stream().filter(student -> student.getSex() == 2).count());
        Assert.assertEquals(1, resultList.stream().filter(student -> student.getSex() == 1 && student.getAge() >= 20).count());
    }

    /**
     * Where包含and和or
     * 包括Limit
     */
    @Test
    public void testQuery004() {
        Where where = Where.builder().criteriaString("age>=20 and sex=1 or sex=2").build();
        OrderBy orderBy = OrderBy.builder().build();
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(2).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(ArrayList.class.isInstance(result));

        List<Student> resultList = (ArrayList<Student>) result;
        Assert.assertEquals(2, resultList.size());
    }

    /**
     * 包括Limit
     * 包括Sort
     */
    @Test
    public void testQuery005() {
        Where where = Where.builder().build();
        OrderBy orderBy = OrderBy.builder().orderString("name asc,age desc").build();
        GroupBy groupBy = GroupBy.builder().build();
        Limit limit = Limit.builder().size(10).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(ArrayList.class.isInstance(result));

        List<Student> resultList = (ArrayList<Student>) result;
        Assert.assertEquals(5, resultList.size());
        Assert.assertEquals(23, resultList.get(1).getAge().intValue());
        Assert.assertEquals("Bob", resultList.get(0).getName());
    }

    /**
     * 包括Limit
     * 包括Sort
     * 包括group by
     */
    @Test
    public void testQuery006() {
        Where where = Where.builder().build();
        OrderBy orderBy = OrderBy.builder().orderString("age desc").build();
        GroupBy groupBy = GroupBy.builder().groupString("sex,age").build();
        Limit limit = Limit.builder().size(Long.MAX_VALUE).build();

        List<Object> inputDataList = DataProvider.getStudents();
        Object result = database.query(inputDataList, where, orderBy, groupBy, limit);

        Assert.assertTrue(HashMap.class.isInstance(result));
        Map<Integer, Map<Integer, List<Student>>> sexMap = (Map<Integer, Map<Integer, List<Student>>>) result;

        //男和女两条数据
        Assert.assertEquals(2, sexMap.size());

        //女生有3名
        Assert.assertEquals(3, sexMap.get(2).size());
        //有一位23岁的女生
        Assert.assertEquals("Cassie", sexMap.get(2).get(23).get(0).getName());
        System.out.println(new Gson().toJson(result));
    }
}
