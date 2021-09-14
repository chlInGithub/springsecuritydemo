package com.example.security_demo.controller;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.security_demo.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    HttpServletRequest HttpServletRequest;
    @Resource
    HttpServletResponse httpServletResponse;

    /**
     * 验证 mvc集成  简洁获取用户信息
     */
    @GetMapping("/check")
    public void login(@AuthenticationPrincipal CustomUserDetails customUser) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        System.out.println("login post");
    }

    /**
     * 通过访问get /login获取csrf-token
     */
    @GetMapping
    public void loginHtml() throws IOException {
        httpServletResponse.sendRedirect("/login.html");
    }
}
