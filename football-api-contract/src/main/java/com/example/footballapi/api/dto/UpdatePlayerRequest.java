package com.example.footballapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdatePlayerRequest(
        @NotBlank(message = "Имя не может быть пустым")
        String firstName,

        @NotBlank(message = "Фамилия не может быть пустой")
        String lastName,

        @NotNull(message = "Дата рождения не может быть нулевой")
        LocalDate dateOfBirth,

        @NotBlank(message = "Позиция не может быть пустой")
        String position
) {}