package com.example.security_demo.config;

import javax.annotation.Resource;

import com.example.security_demo.config.bean.CSRFRequestMatcher;
import com.example.security_demo.config.bean.CustomLogoutHandler;
import com.example.security_demo.config.bean.CustomSecurityContextRepository;
import com.example.security_demo.config.bean.NewCsrfTokenFilter;
import com.example.security_demo.config.bean.authentication.CustomAuthenticationSuccessHandler;
import com.example.security_demo.config.bean.authorization.CustomAccessDecisionManager;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableWebSecurity
@ConfigurationProperties("websecurityconfig")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * csrfToken最大max age
     */
    @Setter
    Integer csrfTokenCookieMaxAge;

    @Resource
    NewCsrfTokenFilter newCsrfTokenFilter;

    /**
     * 对用提交的password进行编码  以便与userdetails中password进行对比。
     * TODO 据实际password加密方式而定
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 自定义获取当事人的方式，例如调用rpc
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                // TODO 从数据源查询
                CustomUserDetails customUserDetails = new CustomUserDetails();
                customUserDetails.setUsername("user");
                customUserDetails.setPassword("password");
                return customUserDetails;
            }
        };

        return userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

        // spring security将忽略如下url格式
        web.ignoring().antMatchers("/**.html", "/", "/home");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        ((CookieCsrfTokenRepository)csrfTokenRepository).setCookieMaxAge(csrfTokenCookieMaxAge);

        // 与csrfFilter使用同一个csrfTokenRepository
        newCsrfTokenFilter.setCsrfTokenRepository(csrfTokenRepository);

        http.authorizeRequests()
                // 首页 等其他  不需要安全验证
                //.antMatchers("/", "/home").permitAll()
                // 重定向到登录html  不需要安全验证，通过访问/login获取csrf-token
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                // 其他url 必须身份认证和授权验证
                .anyRequest().authenticated()
                // 自定义授权验证
                .accessDecisionManager(new CustomAccessDecisionManager())
                .and()
                // 规定：登录form post url，身份认证成功控制器，注意successHandler和successForwardUrl不能同时设置
                .formLogin().loginPage("/login").successHandler(new CustomAuthenticationSuccessHandler()).permitAll()
                .and()
                // logout 自定义部分
                .logout().logoutUrl("/logout").addLogoutHandler(new CustomLogoutHandler()).permitAll()
                .and()
                // csrf
                .csrf().requireCsrfProtectionMatcher(new CSRFRequestMatcher())
                .csrfTokenRepository(csrfTokenRepository)
                .and()
                // 自定义个filter，针对某些url生成新的csrfToken
                .addFilterAfter(newCsrfTokenFilter, CsrfFilter.class)
                // 自定义securityContext存储的位置  例如实现分布式session
                .securityContext().securityContextRepository(new CustomSecurityContextRepository());
    }
}
