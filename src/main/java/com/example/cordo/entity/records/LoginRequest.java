package com.example.cordo.entity.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "Почта должна являться почтой!")
        @Size(min = 1,max = 60,message = "Почта должна быть от 1 до 60 символов!")
        String email,
        @Size(min = 6,max = 500,message = "Пароль должен быть от 6 до 500 символов!")
        String password
) {}