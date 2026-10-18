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
public class Burmalda {
    private String hypnoDancer = "ffffffffffffffffffffffffffffffff";
    private long tr = 55555;
    public SecretKey getKey() {return Keys.hmacShaKeyFor(hypnoDancer.getBytes());}
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String extractEmail(String token) {return parseClaims(token).getSubject();}
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object roles = parseClaims(token).get("roles");
        return roles instanceof List<?> list ? (List<String>) list : List.of();
    }
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .claim("roles",roles)
                .issuedAt(new Date())
                .expiration(new Date (System.currentTimeMillis() + tr))
                .signWith(getKey())
                .subject(userDetails.getUsername())
                .compact();
    }
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
