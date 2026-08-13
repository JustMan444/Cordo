package com.example.cordo.service;

import com.example.cordo.BillingScheduler;
import com.example.cordo.Entity.Subscribe;
import com.example.cordo.Entity.User;
import com.example.cordo.Entity.UserDTO;
import com.example.cordo.Entity.UserSubscription;
import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.repository.jpa.UserSubscriptionRepository;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.repository.redis.PlanRepository;
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
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

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
    @Transactional
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
    public UserDTO topOpBalance(User user,int amount) {
        if(user == null) {
            logger.warn("User not found");
            throw new IllegalArgumentException("User cannot be null");
        }
        if(user.getBalance() + amount > maxBalance) {
            logger.debug("Very big balance error bad request");
            throw new BalanceLimitExceededException("Balance limit exceeded");
        }
        user.setBalance(user.getBalance() + amount);
        User realuser = usersRepository.save(user);
        UserDTO userDTO = new UserDTO(realuser);
        return userDTO;
    }
    @Transactional
    public UserDTO buySubcribe(User user,String planID) {
        if(user == null) {
            logger.warn("user not found");
            throw new IllegalArgumentException("User cannot be null");
        }
        Subscribe subscribe = planRepository.findById(planID).orElse(null);//redisTemplate.
        if(subscribe == null) {
            logger.warn("plan not found");
            throw  new IllegalArgumentException("plan cannot be null");
        }
        if(user.getBalance() < subscribe.getMoneyInMonth()) {
            logger.warn("balance is too little");
            throw new BalanceLimitExceededException("balance very little");
        }
        user.setBalance(user.getBalance() - subscribe.getMoneyInMonth());
        User real = usersRepository.save(user);
        UserSubscription sub = new UserSubscription(user.getUserId(), planID, "ACTIVE");
        sub.setNextBillingDate(java.time.LocalDateTime.now().plusMonths(1));
        userSubscriptionRepository.save(sub);
        return new UserDTO(real);
    }
}
