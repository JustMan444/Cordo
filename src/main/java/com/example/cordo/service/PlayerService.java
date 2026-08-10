package com.example.cordo.service;

import com.example.cordo.BillingScheduler;
import com.example.cordo.Entity.User;
import com.example.cordo.Entity.UserDTO;
import com.example.cordo.repository.jpa.UsersRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PlayerService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BillingScheduler.class);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BaseSecurity baseSecurity;

    public UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }
    public User getUser(String password, String email) {
        User user = new User(password,email);
        return user;
    }

    public UserDTO regNewUser(String password, String email) {
        String realPassword = baseSecurity.encodePassword(password);
        User user = new User(realPassword,email);
        logger.info("New User has register");
        usersRepository.save(user);
        UserDTO dto = new UserDTO(user);
        return dto;
    }
    public User getUserFromDT(String userID) {
        User user = usersRepository.findById(userID).orElse(null);
        return user;
    }
    public void SaveUserInDT(User user) {
        usersRepository.save(user);
    }
}
