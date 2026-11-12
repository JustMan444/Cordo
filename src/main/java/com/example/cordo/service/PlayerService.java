package com.example.cordo.service;

import com.example.cordo.entity.*;
import com.example.cordo.entity.dto.UserDTO;
import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.exception.PlanNotFoundException;
import com.example.cordo.exception.UserNotFoundException;
import com.example.cordo.repository.jpa.UserSubscriptionRepository;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.repository.redis.PlanRepository;
import com.example.cordo.security.BaseSecurity;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PlayerService {

    /**
     * <p>
     * <b><h1>Сервис управления пользователями</h1></b>
     * </p>
     * <p>
     * <h3>Методы:</h3>
     * </p>
     * <p>
     * <ul>
     * <li>Регистрирование нового пользователя {@link PlayerService#regNewUser(String, String)}</li>
     * <li>Пополнения баланса {@link PlayerService#topUpBalance(String, BigDecimal)}</li>
     * <li>Покупка подписки {@link PlayerService#buySubcribe(String, String)}</li>
     * </ul>
     * </p>
     * @version 1.1
     * @author JustMan444
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private UsersRepository usersRepository;
    private BaseSecurity baseSecurity;
    private PlanRepository planRepository;
    private UserSubscriptionRepository userSubscriptionRepository;
    private int maxBalance;
    @Autowired
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
    public PlayerService() {}


    @Transactional
    public UserDTO regNewUser(String password, String email) {
        String realPassword = baseSecurity.encodePassword(password);
        User user = new User(realPassword,email);
        user.setRole(Role.USER);
        logger.info("New User has register");
        usersRepository.saveAndFlush(user);
        UserDTO dto = new UserDTO(user);
        return dto;
    }
    @Transactional
    public UserDTO topUpBalance(String email,BigDecimal amount) {
        User user = usersRepository.findByEmailForUpdate(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));;
        if(user.getBalance().add(amount).compareTo(new BigDecimal(maxBalance)) >= 1) {
            logger.debug("Very big balance error bad request");
            throw new BalanceLimitExceededException("Balance limit exceeded");
        }
        user.setBalance(user.getBalance().add(amount));

        User realuser = usersRepository.saveAndFlush(user);
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
        if(user.getBalance().add(new BigDecimal(amount)).compareTo(new BigDecimal(maxBalance)) >= 1) {
            logger.debug("Very big balance error bad request");
            throw new BalanceLimitExceededException("Balance limit exceeded");
        }
        user.setBalance(user.getBalance().add(new BigDecimal(amount)));

        User realuser = new User("а","g");//usersRepository.save(user);
        UserDTO userDTO = new UserDTO(realuser);
        return new UserDTO(new User());
    }
    @Transactional
    public UserDTO buySubcribe(String email, String planID) {
        User user = usersRepository.findByEmailForUpdate(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Subscribe subscribe = planRepository.findById(planID)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found"));

        if(user.getBalance().compareTo(subscribe.getMoneyInMonth()) == -1) {
            logger.warn("balance is too little");
            throw new BalanceLimitExceededException("balance very little");
        }
        user.setBalance(user.getBalance().subtract(subscribe.getMoneyInMonth()));
        User real = usersRepository.saveAndFlush(user);
        UserSubscription sub = new UserSubscription(user.getUserId(), planID, "ACTIVE");
        sub.setNextBillingDate(java.time.LocalDateTime.now().plusMonths(1));
        userSubscriptionRepository.save(sub);
        return new UserDTO(real);
    }
}
