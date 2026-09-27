package com.example.cordo.records;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank(message = "UserId cannot be null")
        String userId,
        @NotBlank(message = "PlanId cannot be null")
        String planId

) {}
