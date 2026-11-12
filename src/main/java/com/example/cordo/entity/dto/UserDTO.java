package com.example.cordo.entity.dto;


import com.example.cordo.entity.User;

import java.math.BigDecimal;

public class UserDTO {
    private BigDecimal balance;
    private String email;
    private String userId;


    public UserDTO(User user) {
        this.balance = user.getBalance();
        this.email = user.getEmail();
        this.userId = user.getUserId();
    }


    public BigDecimal getBalance() {return balance;}

    public String getEmail() {return email;}

    public String getUserId() {return userId;}
}
