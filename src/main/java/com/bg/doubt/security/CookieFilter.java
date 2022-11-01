package com.bg.doubt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();

        if(cookies == null){
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("accessToken")){
                accessToken = cookie.getValue();
            }

            if(cookie.getName().equals("refreshToken")){
                refreshToken = cookie.getValue();
            }
        }

        tokenValidation(accessToken, refreshToken, (HttpServletResponse) response);

        filterChain.doFilter(request, response);
    }

    private void tokenValidation(String accessToken, String refreshToken, HttpServletResponse response){
        TokenManager.TokenValidation result = tokenManager.validateToken(accessToken);

        if(result.equals(TokenManager.TokenValidation.FALSE)){
            return;
        }

        if(result.equals(TokenManager.TokenValidation.EXPIRED)){
            accessToken = tokenRefresh(refreshToken);

            if(accessToken == null){
                return;
            }

            response.addCookie(new CookieBuilder().buildAccessTokenCookie(accessToken));
        }

        Authentication authentication = tokenManager.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String tokenRefresh(String refreshToken){
        TokenManager.TokenValidation result = tokenManager.validateToken(refreshToken);

        if(result.equals(TokenManager.TokenValidation.TRUE)) {
            String userId = tokenManager.getUserId(refreshToken);
            return tokenManager.createAccessToken(userId);
        }

        return null;
    }

}
