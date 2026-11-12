package com.example.cordo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.math.BigDecimal;

@RedisHash("Plan")
public class Subscribe implements Serializable {

    private BigDecimal moneyInMonth;

    @Id
    private String name;

    public Subscribe(BigDecimal moneyInMonth, String name) {
        this.moneyInMonth = moneyInMonth;
        this.name = name;
    }

    public Subscribe() {

    }

    public BigDecimal getMoneyInMonth() {
        return moneyInMonth;
    }

    public void setMoneyInMonth(BigDecimal moneyInMonth) {
        this.moneyInMonth = moneyInMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
