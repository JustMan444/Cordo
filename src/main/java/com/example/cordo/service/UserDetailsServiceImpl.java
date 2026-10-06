package com.example.cordo.service;

import com.example.cordo.entity.User;
import com.example.cordo.exception.UserOptimisticLockingException;
import com.example.cordo.repository.jpa.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UsersRepository userRepository;
    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.userRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user;
        try {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        } catch (UserOptimisticLockingException e) {
            throw new UserOptimisticLockingException("Currently, the user field is occupied by another process.");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
}