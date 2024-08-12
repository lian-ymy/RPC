package com.example.rpccore.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储本地的类名与实现类的对应关系
 */
public class LocalRegistry {
    /**
     * 定义注册信息存储map
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String serviceName,Class<?> serverClass) {
        map.put(serviceName,serverClass);
    }

    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
