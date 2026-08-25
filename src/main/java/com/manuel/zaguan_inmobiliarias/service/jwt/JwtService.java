package com.manuel.zaguan_inmobiliarias.service.jwt;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value ("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getKey() { return Keys.hmacShaKeyFor(secret.getBytes());}

    private String generateToken (String email, UserRol userRol, Long userId, String username){
        return Jwts.builder()
                .subject(email)
                .claim("role", userRol.name())
                .claim("userId", userId)
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid (String token){
        try{
            Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

