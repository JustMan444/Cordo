package com.example.cordo.controller;

import com.example.cordo.entity.records.RegisterRequest;
import com.example.cordo.entity.records.SubscribeRequest;
import com.example.cordo.entity.records.TopUpRequest;
import com.example.cordo.entity.dto.UserDTO;
import com.example.cordo.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Главный RestController
 * Методы:
 * Регистрирует пользователей (/api/v1/users)
 * Пополняет баланс (/api/v1/users/topUp)
 * позволяет оформить покупку подписки (/api/v1/subscriptions/subscribe)
 * Валидация:
 * Присуствует см читать папку records
 * Защита:
 * Присуствует JWT токен права см читать SecurityConfig
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
    public ResponseEntity<UserDTO> amount(
            @Valid @RequestBody TopUpRequest request
            )
    {
        return ResponseEntity.ok(playerService.topUpBalance(request.userId(), request.amount()));
    }
    @PatchMapping("/subscriptions/subscribe")
    public ResponseEntity<UserDTO> buy(
            @Valid @RequestBody SubscribeRequest request
    ) {
        return ResponseEntity.ok(playerService.buySubcribe(request.userId(), request.planId()));
    }
}
