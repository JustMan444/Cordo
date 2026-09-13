package com.example.cordo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    private int balance;
    private String password;
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    public User() {

    }

    public User(int balance, String password, String email, String userId) {
        this.balance = balance;
        this.password = password;
        this.email = email;
        this.userId = userId;
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.balance = 0;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public int getBalance() {return balance;}

    public void setBalance(int balance) {this.balance = balance;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}
}
