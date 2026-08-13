package com.example.cordo.service;

import com.example.cordo.BillingScheduler;
import com.example.cordo.Entity.User;
import com.example.cordo.Entity.UserDTO;
import com.example.cordo.repository.jpa.UsersRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PlayerService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BaseSecurity baseSecurity;

    @Value("${user.maxBalance}")
    private int maxBalance;


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
    @Transactional
    public Object hm(User user,int amount) {
        UserDTO userDTO = new UserDTO(user);
        if(user == null) {
            logger.info("User not found");
            return ResponseEntity.notFound().build();
        }
        if(user.getBalance() + amount > maxBalance) {
            logger.info("Very big balance error bad request");
            return ResponseEntity.badRequest().build();
        }
        user.setBalance(user.getBalance() + amount);
        usersRepository.save(user);
        return ResponseEntity.ok(userDTO);
    }
}
