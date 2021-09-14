package com.example.security_demo.config.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import org.springframework.security.core.context.SecurityContext;

/**
 * session管理器，解耦分布式session方案
 */
public class SessionManager {

    // 假设作为分布式session
    private static Map<String, CustomSession> cache = new ConcurrentHashMap<>();

    /**
     * 自定义session
     */
    @Data
    public static class CustomSession implements Serializable {

        private static final long serialVersionUID = 6307483700990137828L;

        SecurityContext securityContext;

        /**
         * 超时绝对时间
         */
        long expireTime;

        // ...
    }

    public static void setSession(String key, CustomSession customSession){
        cache.put(key, customSession);
    }

    public static CustomSession getSession(String key) {
        return cache.get(key);
    }

    public static boolean contains(String key) {
        return cache.containsKey(key);
    }

    public static void clearSession(String key) {
        cache.remove(key);
    }
}
