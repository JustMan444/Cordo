package com.example.cordo.repository.jpa;

import com.example.cordo.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
}
