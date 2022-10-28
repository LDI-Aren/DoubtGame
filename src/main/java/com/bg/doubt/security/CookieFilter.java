package com.bg.doubt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CookieFilter extends GenericFilterBean {

    private final TokenManager tokenManager;

    public CookieFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String token = null;

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("accessToken")){
                token = cookie.getValue();
            }
        }

        if (token != null) {
            if (tokenManager.validateToken(token)) {
                Authentication authentication = tokenManager.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
