package com.example.cordo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@RedisHash("Plan")
public class Subscribe implements Serializable {

    private int moneyInMonth;

    @Id
    private String name;

    public Subscribe(int moneyInMonth, String name) {
        this.moneyInMonth = moneyInMonth;
        this.name = name;
    }

    public Subscribe() {

    }

    public int getMoneyInMonth() {
        return moneyInMonth;
    }

    public void setMoneyInMonth(int moneyInMonth) {
        this.moneyInMonth = moneyInMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
