package com.example.cordo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {
    private BigDecimal balance;
    private String password;
    private String email;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    public User() {

    }

    public User(BigDecimal balance, String password, String email, String userId) {
        this.balance = balance;
        this.password = password;
        this.email = email;
        this.userId = userId;
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.balance = BigDecimal.valueOf(0);
    }


    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public BigDecimal getBalance() {return balance;}

    public void setBalance(BigDecimal balance) {this.balance = balance;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUserId() {return userId;}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}
}
