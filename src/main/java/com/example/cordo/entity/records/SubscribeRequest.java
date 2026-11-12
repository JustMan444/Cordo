package com.example.cordo.entity.records;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank(message = "PlanId cannot be null")
        String planId

) {}
