package com.example.footballapi.api.endpoints;

import com.example.footballapi.api.dto.StatusResponse;
import com.example.footballapi.api.dto.TeamRequest;
import com.example.footballapi.api.dto.TeamResponse;
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

@Tag(name = "teams", description = "API для работы с командами")
@RequestMapping("/api/teams")
public interface TeamApi {

    @Operation(summary = "Получить все команды")
    @ApiResponse(responseCode = "200", description = "Список команд")
    @GetMapping
    List<TeamResponse> getAllTeams();

    @Operation(summary = "Получить команду по ID")
    @ApiResponse(responseCode = "200", description = "Команда найдена")
    @ApiResponse(responseCode = "404", description = "Команда не найдена", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    TeamResponse getTeamById(
            @Parameter(description = "ID команды для получения", required = true)
            @PathVariable Long id);

    @Operation(summary = "Создать новую команду")
    @ApiResponse(responseCode = "201", description = "Команда успешно создана")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    TeamResponse createTeam(
            @Parameter(description = "Детали команды для создания", required = true)
            @Valid @RequestBody TeamRequest request);

    @Operation(summary = "Обновить команду по ID")
    @ApiResponse(responseCode = "200", description = "Команда обновлена")
    @ApiResponse(responseCode = "404", description = "Команда не найдена", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    TeamResponse updateTeam(
            @Parameter(description = "ID команды для обновления", required = true)
            @PathVariable Long id,
            @Parameter(description = "Новые детали команды", required = true)
            @Valid @RequestBody TeamRequest request);

    @Operation(summary = "Удалить команду по ID")
    @ApiResponse(responseCode = "204", description = "Команда удалена")
    @ApiResponse(responseCode = "404", description = "Команда не найдена")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTeam(@PathVariable Long id);
}