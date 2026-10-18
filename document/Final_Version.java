package com.example.cordo.controller;

import com.example.cordo.entity.records.LoginRequest;
import com.example.cordo.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//Финальный штурм
import java.util.Map;
//Помните: ни слова по-русски!
@RestController
@RequestMapping("/auth/v1/")
public class Final_Version {
    private JwtService jwtService;
    private AuthenticationManager authManager;
    public Final_Version(JwtService jwtService,AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }
    @PostMapping("login/")
    public Map<String,String> login(@RequestParam LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return Map.of("token",token);
    }
}
