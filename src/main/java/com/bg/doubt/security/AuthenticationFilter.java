package com.bg.doubt.security;

import com.bg.doubt.user.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    TokenManager tokenManager;

    public AuthenticationFilter(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest credit = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            log.info("Autentication : " + credit.getUserId());

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credit.getUserId(),
                            credit.getUserPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String userId = ((User)authResult.getPrincipal()).getUsername();
        String accessToken = tokenManager.createAccessToken(userId);
        String refreshToken = tokenManager.createRefreshToken(userId);

        response.addCookie(new CookieBuilder().buildAccessTokenCookie(accessToken));
        response.addCookie(new CookieBuilder().buildRefreshTokenCookie(refreshToken));
    }


}
