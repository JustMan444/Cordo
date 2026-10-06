package com.example.cordo.entity.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email cannot be null")
        @Email(message = "Некорректный формат Email адреса")
        String email,

        @NotBlank(message = "Пароль не может быть равен null")
        @Size(min = 6,max = 100,message = "пароль обязан содержать от 6 до 100 символов")
        String password

) {}