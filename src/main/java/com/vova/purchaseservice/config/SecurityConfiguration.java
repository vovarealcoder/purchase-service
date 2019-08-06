package com.vova.purchaseservice.config;

import com.vova.purchaseservice.security.AuthorizationService;
import com.vova.purchaseservice.security.BasicAuth;
import com.vova.purchaseservice.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().mvcMatchers("/user/register").permitAll()
                .mvcMatchers("/service/**", "/user/info/**", "/user/change/**").authenticated()
                .and().httpBasic().authenticationEntryPoint(basicAuthorizer());

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication)
            throws Exception {
        authentication.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    protected AuthorizationService userDetailsService() {
        return new AuthorizationService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicAuth basicAuthorizer() {
        return new BasicAuth();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public LoginAttemptService attemptService() {
        return new LoginAttemptService();
    }

}