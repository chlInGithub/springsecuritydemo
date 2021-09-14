package com.example.security_demo.config.bean.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.security_demo.config.CustomUserDetails;
import com.example.security_demo.config.bean.SessionManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 身份认证成功控制器
 * <br/>
 * 存储到分布式session
 * <br/>
 * 设置cookie，如TOKEN，作为分布式session的key
 */
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        if (null == context || null == context.getAuthentication()) {
            return;
        }

        if (context.getAuthentication() instanceof AnonymousAuthenticationToken){
            return;
        }

        String tokenKey = "T" + context.getAuthentication().hashCode();
        SessionManager.CustomSession customSession = new SessionManager.CustomSession();

        CustomUserDetails principal = (CustomUserDetails) context.getAuthentication().getPrincipal();
        principal.setPassword(null);

        // WebAuthenticationDetails details = (WebAuthenticationDetails) context.getAuthentication().getDetails();

        customSession.setSecurityContext(context);
        // customSession.setExpireTime(xxxx);
        SessionManager.setSession(tokenKey, customSession);

        Cookie cookie = new Cookie("TOKEN", tokenKey);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
