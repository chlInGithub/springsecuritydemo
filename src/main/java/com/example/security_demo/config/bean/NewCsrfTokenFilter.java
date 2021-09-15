package com.example.security_demo.config.bean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 针对某些url，必须生成新的csrfToken，例如填写表单前。
 */
@Component
@ConfigurationProperties(prefix = "newcsrftoken")
public class NewCsrfTokenFilter extends OncePerRequestFilter {

    @Setter
    private CsrfTokenRepository csrfTokenRepository;

    /**
     * 访问这些url，会生成信息的csrfToken
     * @param urlParttern
     */
    public void setUrlParttern(String urlParttern) {
        String[] split = urlParttern.split(",");
        if (split.length < 1) {
            return;
        }

        for (String s : split) {
            urlPartterns.add(s);
        }
    }

    Set<String> urlPartterns = new HashSet<>();


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return !urlPartterns.contains(requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);

        filterChain.doFilter(request, response);
    }
}
