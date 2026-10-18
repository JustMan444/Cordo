package com.example.cordo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class BurmaldaV3 {
    private String secret = "тут должны быть 32 символа но мне лень их писать";
    private long krsix = 777_666_333;
    public SecretKey getKey() {return Keys.hmacShaKeyFor(secret.getBytes());}
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles",roles)
                .signWith(getKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + krsix))
                .compact();
    }
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object roles = parseClaims(token).get("roles");
        return roles instanceof List<?> list ? (List<String>) list : List.of(); //посмотрел лишь один раз ведь забыл как пишется instanceof но прошу заметить что я знал что он нужен
    }
}
