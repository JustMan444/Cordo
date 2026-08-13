package com.example.cordo.controller;

import com.example.cordo.BillingScheduler;
import com.example.cordo.Entity.Subscribe;
import com.example.cordo.Entity.User;
import com.example.cordo.Entity.UserDTO;
import com.example.cordo.Entity.UserSubscription;
import com.example.cordo.repository.redis.PlanRepository;
import com.example.cordo.repository.jpa.UserSubscriptionRepository;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.service.BaseSecurity;
import com.example.cordo.service.PlayerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class UserController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private BaseSecurity baseSecurity;
    @Autowired
    private PlayerService playerService;



    @PostMapping("/api/v1/users")
    public ResponseEntity<UserDTO> registering(
            @RequestParam("email") String userEmail,
            @RequestParam("password") String userPassword
    ) {
        return ResponseEntity.ok(playerService.regNewUser(userPassword,userEmail));
    }
    @PostMapping("/api/v1/users/topup")
    public ResponseEntity<UserDTO> amount(
            @RequestParam("userId") String userID,
            @RequestParam("amount") int amount
    )
    {
        playerService.hm(playerService.getUserFromDT(userID),amount);
    }
    @PostMapping("/api/v1/subscriptions/subscribe")
    public ResponseEntity<UserDTO> buy(
            @RequestParam("userId") String userID,
            @RequestParam("planId") String planID
    ) {
        User user = playerService.getUserFromDT(userID);
        if(user == null) {
            logger.info("user not found");
            return ResponseEntity.notFound().build();
        }
        Subscribe subscribe = planRepository.findById(planID).orElse(null);//redisTemplate.
        if(subscribe == null) {
            logger.info("plan not found");
            return ResponseEntity.notFound().build();
        }
        if(user.getBalance() < subscribe.getMoneyInMonth()) {
            logger.info("balance is too little");
            return ResponseEntity.badRequest().build();
        }
        user.setBalance(user.getBalance() - subscribe.getMoneyInMonth());
        playerService.SaveUserInDT(user);
        UserSubscription sub = new UserSubscription(userID, planID, "ACTIVE");
        sub.setNextBillingDate(java.time.LocalDateTime.now().plusMonths(1));
        userSubscriptionRepository.save(sub);
        return ResponseEntity.ok(playerService.getUserDTO(user));
    }
}
