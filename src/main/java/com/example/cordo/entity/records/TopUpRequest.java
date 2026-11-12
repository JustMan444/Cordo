package com.example.cordo.entity.records;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TopUpRequest(
        @DecimalMin(value = "0.01", message = "Сумма пополнения должна быть больше нуля")
        @NotNull(message = "Сумма пополнения не должна быть пустой")
        @Digits(integer = 8, fraction = 2, message = "Некорректный формат числа максимум 8 цифр до запятой и 2 после")

        BigDecimal amount
) {}