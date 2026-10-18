package com.example.cordo.controller;

import com.example.cordo.entity.records.LoginRequest;
import com.example.cordo.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
//Если долго мучится что нибудь получится
//И помните Не слова по русски

@RestController
@RequestMapping("security/api/v1")
public class Raz_Dva_Nachinay_Po_Novoy {
    private JwtService jwtService;
    private AuthenticationManager authManager;
    public Raz_Dva_Nachinay_Po_Novoy(JwtService jwtService,AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }
    @PostMapping("login")
    public Map<String,String> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return Map.of("token",token);
    }
}
