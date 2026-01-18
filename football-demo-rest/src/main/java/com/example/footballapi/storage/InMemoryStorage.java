package com.example.footballapi.storage;

import com.example.footballapi.api.dto.PlayerRequest;
import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.api.dto.TeamRequest;
import com.example.footballapi.api.dto.TeamResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, PlayerResponse> players = new ConcurrentHashMap<>();
    public final Map<Long, TeamResponse> teams = new ConcurrentHashMap<>();

    public final AtomicLong playerSequence = new AtomicLong(0);
    public final AtomicLong teamSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        PlayerResponse player1 = new PlayerResponse(
                playerSequence.incrementAndGet(),
                "Иван",
                "Иванов",
                LocalDate.of(1990, 5, 15),
                "Нападающий"
        );

        PlayerResponse player2 = new PlayerResponse(
                playerSequence.incrementAndGet(),
                "Петр",
                "Петров",
                LocalDate.of(1992, 7, 20),
                "Защитник"
        );

        players.put(player1.getId(), player1);
        players.put(player2.getId(), player2);

        TeamResponse team1 = new TeamResponse(
                teamSequence.incrementAndGet(),
                "Россия",
                "Стадион Лужники",
                LocalDate.of(1995, 10, 21).atStartOfDay(),
                new ArrayList<>(List.of(player1))
        );

        TeamResponse team2 = new TeamResponse(
                teamSequence.incrementAndGet(),
                "Германия",
                "Стадион Олимпийский",
                LocalDate.of(1990, 2, 28).atStartOfDay(),
                new ArrayList<>(List.of(player2))
        );

        teams.put(team1.getId(), team1);
        teams.put(team2.getId(), team2);
    }

    public PlayerResponse createPlayer(PlayerRequest request) {
        Long playerId = playerSequence.incrementAndGet();
        PlayerResponse player = new PlayerResponse(
                playerId,
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.position()
        );
        players.put(playerId, player);
        return player;
    }

    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        if (!players.containsKey(id)) {
            return null;
        }
        PlayerResponse updatedPlayer = new PlayerResponse(
                id,
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.position()
        );
        players.put(id, updatedPlayer);
        return updatedPlayer;
    }

    public void deletePlayer(Long id) {
        players.remove(id);
        // Удаляем игрока из всех команд
        for (TeamResponse team : teams.values()) {
            if (team.getPlayers() != null) {
                team.getPlayers().removeIf(player -> player.getId().equals(id));
            }
        }
    }

    public TeamResponse createTeam(TeamRequest request) {
        Long teamId = teamSequence.incrementAndGet();
        TeamResponse team = new TeamResponse(
                teamId,
                request.name(),
                request.stadium(),
                request.founded(),
                new ArrayList<>()
        );
        teams.put(teamId, team);
        return team;
    }

    public TeamResponse updateTeam(Long id, TeamRequest request) {
        if (!teams.containsKey(id)) {
            return null;
        }
        TeamResponse updatedTeam = new TeamResponse(
                id,
                request.name(),
                request.stadium(),
                request.founded(),
                new ArrayList<>()
        );
        teams.put(id, updatedTeam);
        return updatedTeam;
    }

    public void deleteTeam(Long id) {
        teams.remove(id);
    }
}
