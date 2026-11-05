package com.example.cordo.controller;

import com.example.cordo.entity.records.RegisterRequest;
import com.example.cordo.entity.records.SubscribeRequest;
import com.example.cordo.entity.records.TopUpRequest;
import com.example.cordo.entity.dto.UserDTO;
import com.example.cordo.security.JwtAuthFilter;
import com.example.cordo.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** <h1>Главный RestController</h1>
 * <p>
 * <h2>Методы: </h2>
 * <ul>
 * <li>Регистрирует пользователей (/api/v1/users) {@link UserController#registerUser(RegisterRequest)  regUser}</li>
 * <li>Пополняет баланс (/api/v1/users/topUp) {@link UserController#topUpUserBalance(TopUpRequest) topUpUserBalance}</li>
 * <li>Позволяет оформить покупку подписки (/api/v1/subscriptions/subscribe) {@link UserController#buy_subscribe(SubscribeRequest) buySubscribe}</li>
 * </ul>
 * </p>
 * <p>
 * <h2>Валидация: </h2>
 * <ul>
 * <li>Присуствует см читать папку records {@link com.example.cordo.entity.records.LoginRequest logRequest} {@link RegisterRequest regRequest} {@link SubscribeRequest subcriveRequest} {@link TopUpRequest topUpRequest}</li>
 * </ul>
 * </p>
 * <p>
 * <h2>Защита: </h2>
 * <ul>
 * <li>Присуствует JWT токен {@link JwtAuthFilter JwtFilter}, права {@link com.example.cordo.entity.Role roles},см читать SecurityConfig {@link com.example.cordo.config.SecurityConfig securityConfig}</li>
 * </ul>
 * </p>
 *
 * @since Beta 0.9
 * @version 2.0
 * @author JustMan444
 *
 */


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final PlayerService playerService;
    public UserController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> registerUser(
           @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.regNewUser(request.password(),request.email()));
    }
    @PatchMapping("/users/topUp")
    public ResponseEntity<UserDTO> topUpUserBalance( //запомним тут был amount но пришлось простится
            @Valid @RequestBody TopUpRequest request,
            Authentication authentication
            )
    {
        return ResponseEntity.ok(playerService.topUpBalance(authentication.getName(), request.amount()));
    }
    @PatchMapping("/subscriptions/subscribe")
    public ResponseEntity<UserDTO> buy_subscribe(
            @Valid @RequestBody SubscribeRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.buySubcribe(authentication.getName(), request.planId()));
    }
}
