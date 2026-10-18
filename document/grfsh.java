package com.example.cordo.service;

import com.example.cordo.entity.User;
import com.example.cordo.exception.UserNotFoundException;
import com.example.cordo.repository.jpa.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class grfsh implements UserDetailsService {
    UsersRepository usersRepository;
    public grfsh(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //почему тут userName а мы email да в нашем проекте нет имен но все же
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .authorities("ROLE_" + user.getRole().name())
                .password(user.getPassword())
                .build();
    }
}
//Все написал с лишь одним подглядыванием за этим говном org.springframework.security.core.userdetails.User это надо запомнить а еще вопрос
