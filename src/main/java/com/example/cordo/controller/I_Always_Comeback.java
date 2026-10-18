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

//И помните Не слова по русски!

@RestController
@RequestMapping("/api/v1/auth") //"security/api/v1"
public class I_Always_Comeback {
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public I_Always_Comeback(JwtService jwtService,AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }
    @PostMapping("login/")
    public Map<String,String> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),request.password())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return Map.of("token",token);
    }
}
