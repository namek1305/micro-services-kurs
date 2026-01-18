package com.example.footballapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record TeamRequest(
        @NotBlank(message = "Название не может быть пустым")
        String name,
        @NotBlank(message = "Стадион не может быть пустым")
        String stadium,
        @NotNull(message = "Дата основание не может быть нулевой")
        LocalDateTime founded,
        List<Long> playerIds
) {}