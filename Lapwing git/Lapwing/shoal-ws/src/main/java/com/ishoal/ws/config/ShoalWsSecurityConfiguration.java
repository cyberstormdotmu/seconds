package com.ishoal.ws.config;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;

import com.ishoal.ws.security.CsrfHeaderFilter;
import com.ishoal.ws.security.LoginUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class ShoalWsSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "loginUserDetailsService")
    private LoginUserDetailsService userDetailsService;


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .formLogin().disable()
                .authorizeRequests()
                    .antMatchers("/ws/login").permitAll()
                    .antMatchers("/ws/registrationconfirm/**").permitAll()
                    .antMatchers("/ws/registration").permitAll()
                    .antMatchers("/ws/contact").permitAll()
                    .antMatchers("/ws/resetpassword/**").permitAll()
                    .antMatchers("/ws/admin/**").hasAnyRole("ADMIN","SUPPLIER")
                    .antMatchers("/ws/**").hasAnyRole("BUYER", "ADMIN")             
                    .anyRequest().authenticated()
                    .and()
                    .addFilterAfter(new CsrfHeaderFilter(), SessionManagementFilter.class)
                .csrf()
            .disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}