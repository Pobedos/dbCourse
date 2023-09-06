package ru.pobedonostsev.dbcourse.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtSecurityConfigurer
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private JwtFilter filter;

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.exceptionHandling()
                .authenticationEntryPoint(new JwtAuthEntryPoint())
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//                .addFilterBefore(filterChainExceptionHandler, JwtFilter.class);
    }


}
