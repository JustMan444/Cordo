package com.example.cordo.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String planName;
    private String status;
    private java.time.LocalDateTime nextBillingDate;

    public UserSubscription(Long id, String userId, String planName, String status) {
        this.id = id;
        this.userId = userId;
        this.planName = planName;
        this.status = status;
    }

    public UserSubscription(Long id, String planName, String userId, LocalDateTime nextBillingDate, String status) {
        this.id = id;
        this.planName = planName;
        this.userId = userId;
        this.nextBillingDate = nextBillingDate;
        this.status = status;
    }

    public UserSubscription(String userId, String planName, String status) {
        this.userId = userId;
        this.planName = planName;
        this.status = status;
    }

    public UserSubscription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }
}
