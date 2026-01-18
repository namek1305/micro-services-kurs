package com.example.footballapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PlayerRequest(
        @NotBlank(message = "Имя не может быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не может быть пустой")
        String lastName,

        @NotNull(message = "Дата рождения не должна быть нулевой")
        LocalDate dateOfBirth,

        @NotBlank(message = "Позиция не может быть пустой")
        String position
) {}