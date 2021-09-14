package com.example.security_demo.config.bean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * request 满足什么条件时，需要验证csrfToken
 */
public class CSRFRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        boolean match =
                httpServletRequest.getRequestURI().equals("/login") && httpServletRequest.getMethod()
                        .toLowerCase().equals("post");
        if (match) {
            return match;
        }

        match = httpServletRequest.getRequestURI().equals("/logout") && httpServletRequest.getMethod()
                        .toLowerCase().equals("post");
        // ...

        return match;
    }
}
