package com.customdb.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 反射工具类
 *
 * @author mingbozhang
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * 根据类型获取类型转换方法
     *
     * @param type
     * @return
     */
    public static Object parseValueByType(Class type, String valueToParse) {
        Objects.requireNonNull(type);

        if (type.equals(String.class)) {
            return valueToParse;
        }

        try {
            return getDeclaredMethod(type, "valueOf", String.class).invoke(type, valueToParse);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Method getDeclaredMethod(Class type, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = type.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Field getDeclaredField(Class type, String fieldName) {
        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);

            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("fieldName:" + fieldName, e);
        }
    }

}
