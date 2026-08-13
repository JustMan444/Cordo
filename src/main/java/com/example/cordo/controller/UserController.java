package com.example.cordo.controller;

import com.example.cordo.Entity.RegisterRequest;
import com.example.cordo.Entity.SubscribeRequest;
import com.example.cordo.Entity.TopUpRequest;
import com.example.cordo.Entity.UserDTO;
import com.example.cordo.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final PlayerService playerService;
    public UserController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserDTO> registering(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(playerService.regNewUser(request.password(),request.email()));
    }
    @PostMapping("/api/v1/users/topup")
    public ResponseEntity<UserDTO> amount(
            @RequestBody TopUpRequest request
            )
    {
        return ResponseEntity.ok(playerService.topOpBalance(playerService.getUserFromDT(request.userId()), request.amount()));
    }
    @PostMapping("/api/v1/subscriptions/subscribe")
    public ResponseEntity<UserDTO> buy(
        @RequestBody SubscribeRequest request
    ) {
        return ResponseEntity.ok(playerService.buySubcribe(playerService.getUserFromDT(request.userId()), request.planId()));
    }
}
