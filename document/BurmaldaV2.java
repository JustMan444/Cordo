package com.example.cordo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Year;
import java.util.Date;
import java.util.List;

@Service
public class BurmaldaV2 {
    private String keyr = "fojklfojklfojklfojklfojklfojklfg";
    private long six = 777;
    public SecretKey getkey() {return Keys.hmacShaKeyFor(keyr.getBytes());}
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date (System.currentTimeMillis() + six))
                .signWith(getkey())
                .claim("roles",roles)
                .compact();
    }
    public String extractEmail(String token) {return parseClaims(token).getSubject();}
    public List<String> extractRoles(String token) {
        Object roles = parseClaims(token);
        return roles instanceof List<?> list ? (List<String>) list : List.of();
    }
}
