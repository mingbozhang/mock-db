package com.customdb.test;

import com.customdb.Database;
import com.customdb.keyword.GroupBy;
import com.customdb.keyword.Limit;
import com.customdb.keyword.OrderBy;
import com.customdb.keyword.Where;
import com.customdb.model.Student;
import com.customdb.test.common.DataProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        Where where = Where.builder().criteriaString("age>=20 and sex=1").build();
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
}
