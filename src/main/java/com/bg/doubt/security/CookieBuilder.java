package com.bg.doubt.security;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;

public class CookieBuilder {
    private String name;
    private String value;
    private boolean httpOnly;
    private int maxAge;
    private String domain;

    public CookieBuilder(){
        httpOnly = false;
        maxAge = -1;
    }

    public CookieBuilder name(String name){
        this.name = name;
        return this;
    }

    public CookieBuilder value(String value){
        this.value = value;
        return this;
    }

    public CookieBuilder httpOnly(boolean httpOnly){
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieBuilder maxAge(int maxAge){
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder domain(String domain){
        this.domain = domain;
        return this;
    }

    public Cookie build(){
        Cookie cookie = null;

        if(name != null && value != null) {
            cookie = new Cookie(name, value);
            cookie.setHttpOnly(httpOnly);

            if(maxAge >= 0) {
                cookie.setMaxAge(maxAge);
            }
            if(domain != null) {
                cookie.setDomain(domain);
            }
        }
        return cookie;
    }

    public Cookie buildAccessTokenCookie(String accessToken){
        return this.name("accessToken")
                .value(accessToken)
                .httpOnly(false)
                .maxAge(1800)
                .build();
    }

    public Cookie buildRefreshTokenCookie(String refreshToken){
        return this.name("refreshToken")
                .value(refreshToken)
                .httpOnly(true)
                .maxAge(3600*24)
                .build();
    }

    public Cookie destroyAccessTokenCookie(String accessToken){
        return this.name("refreshToken")
                .value(accessToken)
                .httpOnly(false)
                .maxAge(1)
                .build();
    }

    public Cookie destroyRefreshTokenCookie(String refreshToken){
        return this.name("refreshToken")
                .value(refreshToken)
                .httpOnly(true)
                .maxAge(1)
                .build();
    }
}
