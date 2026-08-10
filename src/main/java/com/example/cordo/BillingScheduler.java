package com.example.cordo;

import com.example.cordo.Entity.Subscribe;
import com.example.cordo.Entity.User;
import com.example.cordo.Entity.UserSubscription;
import com.example.cordo.repository.redis.PlanRepository;
import com.example.cordo.repository.jpa.UserSubscriptionRepository;
import com.example.cordo.repository.jpa.UsersRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;




@Component
public class BillingScheduler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BillingScheduler.class);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private PlanRepository planRepository;


    @Scheduled(cron = "0 * * * * *")
    public void runBilling() {
        Iterable<UserSubscription> allSubs = userSubscriptionRepository.findAll();
        for (UserSubscription sub : allSubs) {
            if(sub.getStatus().equals("ACTIVE") && (sub.getNextBillingDate().isBefore(LocalDateTime.now()) || sub.getNextBillingDate().isEqual(LocalDateTime.now()))) {
                User user = usersRepository.findById(sub.getUserId()).orElse(null);
                if(user == null) {
                    logger.info("Error user not found");
                    continue;
                }
                Subscribe subscribe = planRepository.findById(sub.getPlanName()).orElse(null);
                if(subscribe == null) {
                    logger.info("Error plan not found");
                    continue;
                }
                if(user.getBalance() < subscribe.getMoneyInMonth()) {
                    logger.info("Error");
                    sub.setStatus("SUSPENDED");
                    userSubscriptionRepository.save(sub);
                    continue;
                }
                sub.setNextBillingDate(sub.getNextBillingDate().plusMonths(1));
                user.setBalance(user.getBalance() - subscribe.getMoneyInMonth());
                usersRepository.save(user);
                userSubscriptionRepository.save(sub);
            }
        }
        }
}

