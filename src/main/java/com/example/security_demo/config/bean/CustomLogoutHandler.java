package com.example.security_demo.config.bean;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.util.WebUtils;

/**
 * logout登出控制器
 * <br/>
 * 清除分布式缓存
 * <br/>
 * 删除cookie
 */
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContext context = SecurityContextHolder.getContext();

        if (null != context && null != context.getAuthentication() && !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
            String tokenKey = "T" + context.getAuthentication().hashCode();
            SessionManager.clearSession(tokenKey);
        }

        Cookie cookie = WebUtils.getCookie(request, "TOKEN");
        if (null != cookie) {
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

    }
}
