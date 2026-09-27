package com.example.cordo.service;

import com.example.cordo.entity.*;
import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.exception.UserNotFoundException;
import com.example.cordo.repository.jpa.UserSubscriptionRepository;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.repository.redis.PlanRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    /**
     * Сервер управления пользователями
     * Методы:
     * Регистрирование нового пользователя "regNewUser(String password,String email)"
     * Пополнения баланса "topUpBalance(String id,int amount)"
     * Покупка подписки "buySubcribe( userID,String planID)"
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private UsersRepository usersRepository;
    private BaseSecurity baseSecurity;
    private PlanRepository planRepository;
    private UserSubscriptionRepository userSubscriptionRepository;
    private int maxBalance;
    public PlayerService(UsersRepository usersRepository,
                         BaseSecurity baseSecurity,
                         PlanRepository planRepository,
                         UserSubscriptionRepository userSubscriptionRepository,
                         @Value("${user.maxBalance:20000000}") int maxBalance) {
        this.usersRepository = usersRepository;
        this.baseSecurity = baseSecurity;
        this.planRepository = planRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.maxBalance = maxBalance;
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
    @Transactional
    public UserDTO topUpBalance(String id,int amount) {
        User user = usersRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));;
        if(user.getBalance() + amount > maxBalance) {
            logger.debug("Very big balance error bad request");
            throw new BalanceLimitExceededException("Balance limit exceeded");
        }
        user.setBalance(user.getBalance() + amount);

        User realuser = usersRepository.save(user);
        UserDTO userDTO = new UserDTO(realuser);
        return userDTO;
    }
    //TODO: Когда нибудь распрощаться с topOpBalanceTestFake сильно бессполезным методом который используется исключительно в тестах но мне его жаль
    //TODO: Я не знаю возможно оставлю
    @Transactional
    public UserDTO topOpBalanceTestFake(User user,int amount) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if(user.getBalance() + amount > maxBalance) {
            logger.debug("Very big balance error bad request");
            throw new BalanceLimitExceededException("Balance limit exceeded");
        }
        user.setBalance(user.getBalance() + amount);

        User realuser = new User("а","g");//usersRepository.save(user);
        UserDTO userDTO = new UserDTO(realuser);
        return new UserDTO(new User());
    }
    @Transactional
    public UserDTO buySubcribe(String userId, String planID) {
        User user = usersRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Subscribe subscribe = planRepository.findById(planID)
                .orElseThrow(() -> new IllegalArgumentException("plan cannot be null"));

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
