package com.example.cordo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@RedisHash("Plan")
public class RedisPlan implements Serializable {
    @Id
    private String id;
    private String name;
    private int moneyInMonth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoneyInMonth() {
        return moneyInMonth;
    }

    public void setMoneyInMonth(int moneyInMonth) {
        this.moneyInMonth = moneyInMonth;
    }
}
