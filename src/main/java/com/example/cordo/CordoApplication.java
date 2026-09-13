package com.example.cordo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@EnableRedisRepositories(basePackages = "com.example.cordo.repository.redis")
//@EnableJpaRepositories(basePackages = "com.example.cordo.repository.jpa")
@SpringBootApplication
public class CordoApplication {




	public static void main(String[] args) {
		SpringApplication.run(CordoApplication.class, args);

	}



}
