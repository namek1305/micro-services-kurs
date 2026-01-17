package com.example.footballapi.api.endpoints;

import com.example.footballapi.api.dto.PlayerRequest;
import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.api.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "players", description = "API для работы с футболистами")
@RequestMapping("/api/players")
public interface PlayerApi {

    @Operation(summary = "Получить всех игроков")
    @ApiResponse(responseCode = "200", description = "Список игроков")
    @GetMapping
    List<PlayerResponse> getAllPlayers();

    @Operation(summary = "Получить игрока по ID")
    @ApiResponse(responseCode = "200", description = "Игрок найден")
    @ApiResponse(responseCode = "404", description = "Игрок не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    PlayerResponse getPlayerById(
            @Parameter(description = "ID игрока для получения", required = true)
            @PathVariable Long id);

    @Operation(summary = "Создать нового игрока")
    @ApiResponse(responseCode = "201", description = "Игрок успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    PlayerResponse createPlayer(
            @Parameter(description = "Детали игрока для создания", required = true)
            @Valid @RequestBody PlayerRequest request);

    @Operation(summary = "Обновить игрока по ID")
    @ApiResponse(responseCode = "200", description = "Игрок обновлен")
    @ApiResponse(responseCode = "404", description = "Игрок не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    PlayerResponse updatePlayer(
            @Parameter(description = "ID игрока для обновления", required = true)
            @PathVariable Long id,
            @Parameter(description = "Новые детали игрока", required = true)
            @Valid @RequestBody PlayerRequest request);

    @Operation(summary = "Удалить игрока по ID")
    @ApiResponse(responseCode = "204", description = "Игрок удален")
    @ApiResponse(responseCode = "404", description = "Игрок не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlayer(@PathVariable Long id);
}