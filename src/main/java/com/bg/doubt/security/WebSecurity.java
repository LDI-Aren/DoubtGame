package com.bg.doubt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;


@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationFilter authenticationFilter,
                                           TokenFilter tokenFilter) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/games/**").authenticated()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/home")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
            .addFilter(authenticationFilter)
            .addFilterBefore(tokenFilter,UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(tokenManager);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return authenticationFilter;
    }

    @Bean
    public TokenFilter getTokenFilter(TokenManager tokenManager){
        return new TokenFilter(tokenManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}
