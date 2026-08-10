package com.example.cordo.Entity;


public class UserDTO {
    private int balance;
    private String email;
    private String userId;


    public UserDTO(User user) {
        this.balance = user.getBalance();
        this.email = user.getEmail();
        this.userId = user.getUserId();
    }


    public int getBalance() {return balance;}

    public String getEmail() {return email;}

    public String getUserId() {return userId;}
}
