package com.bg.doubt.security;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
    private static final Long accessDeadline = 1800*1000L;
    private static final Long refreshDeadline = 24*3600*1000L;
    private final String secretKey;

    private final UserDetailsService userDetailsService;

    public TokenManager(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
        secretKey = Base64.encodeBase64String("secret-key".getBytes());
    }

    public String createAccessToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + accessDeadline))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + refreshDeadline))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public TokenValidation validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            if (claims.getBody().getExpiration().before(new Date())) {
                return TokenValidation.EXPIRED;
            }
            return TokenValidation.TRUE;
        } catch (Exception e){
            return TokenValidation.FALSE;
        }
    }

    public String getUserId(String jwtToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return claims.getBody().getSubject();
    }

    public enum TokenValidation{
        EXPIRED, TRUE, FALSE
    }
}
