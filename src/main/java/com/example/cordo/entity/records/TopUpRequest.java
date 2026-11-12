package com.example.cordo.entity.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TopUpRequest(
        @Min(value = 1,message = "Сумма пополнения должна быть больше 0")
        @Max(value = Integer.MAX_VALUE,message = "Сумма пополнения не может быть больше " + Integer.MAX_VALUE)
        int amount
) {}