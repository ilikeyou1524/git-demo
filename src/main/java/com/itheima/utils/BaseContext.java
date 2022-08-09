package com.itheima.utils;

/**
 * @author mimo
 * @description TODO
 * @date 2022-08-05 20:26
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
