package com.example.security_demo.config.bean.authorization;

import java.util.Arrays;

import org.springframework.security.access.vote.AffirmativeBased;

/**
 * 自定义访问控制管理器，继承了AffirmativeBased
 */
public class CustomAccessDecisionManager extends AffirmativeBased {

    public CustomAccessDecisionManager() {
        super(Arrays.asList(new UrlAccessDecisionVoter()));
    }
}
