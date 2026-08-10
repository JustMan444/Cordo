package com.example.cordo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseSecurity {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public String encodePassword(String password) {
        String realPassword = bCryptPasswordEncoder.encode(password);
        return realPassword;
    }
    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return bCryptPasswordEncoder;
    }
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        boolean babuy = bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
        return babuy;
    }
}
