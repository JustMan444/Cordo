package com.example.cordo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.cordo.repository.jpa")
@EnableRedisRepositories(basePackages = "com.example.cordo.repository.redis")
public class MainConfig {
}
