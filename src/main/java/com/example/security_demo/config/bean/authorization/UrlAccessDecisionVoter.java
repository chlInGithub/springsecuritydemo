package com.example.security_demo.config.bean.authorization;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

/**
 * 根据URL进行访问控制
 * <br/>
 * permitAll or anonymous 可通过权限验证
 * <br/>
 * url path作为权限，request的url path 与 当前用户的权限集合 进行比较
 */
public class UrlAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection collection) {
        // permitAll or anonymous 可通过权限验证
        for (Object o : collection) {
            if (o instanceof ConfigAttribute) {
                String attribute = o.toString();
                System.out.println(attribute);
                if ("permitAll".equals(attribute) || "anonymous".equals(attribute)) {
                    return ACCESS_GRANTED;
                }
            }
        }

        // url path作为权限，request的url path 与 当前用户的权限集合 进行比较
        String requestUrl = filterInvocation.getRequestUrl();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (requestUrl.equals(authority.getAuthority())) {
                return ACCESS_GRANTED;
            }
        }
        System.out.println(requestUrl + " deny");
        //throw new AccessDeniedException(requestUrl + " deny");
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
