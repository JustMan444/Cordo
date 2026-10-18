package com.example.cordo.service;

import com.example.cordo.entity.User;
import com.example.cordo.exception.UserNotFoundException;
import com.example.cordo.repository.jpa.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

//Пишем по памяти погнали!
public class Testim implements UserDetailsService {
    UsersRepository usersRepository;
    public Testim(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole())
                .build();
    }
}
