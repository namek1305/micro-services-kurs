package com.example.footballapi.service;

import com.example.footballapi.api.dto.TeamRequest;
import com.example.footballapi.api.dto.TeamResponse;
import com.example.footballapi.api.exception.ResourceNotFoundException;
import com.example.footballapi.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final InMemoryStorage storage;
    private final PlayerService playerService;

    public TeamService(InMemoryStorage storage, @Lazy PlayerService playerService) {
        this.storage = storage;
        this.playerService = playerService;
    }

    public List<TeamResponse> findAll() {
        return new ArrayList<>(storage.teams.values());
    }

    public TeamResponse findById(Long id) {
        return Optional.ofNullable(storage.teams.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
    }

    public TeamResponse create(TeamRequest request) {
        Long id = storage.teamSequence.incrementAndGet();
        TeamResponse team = new TeamResponse(
                id,
                request.name(),
                request.stadium(),
                request.founded(),
                new ArrayList<>()
        );
        storage.teams.put(id, team);
        return team;
    }

    public TeamResponse update(Long id, TeamRequest request) {
        findById(id);
        TeamResponse updatedTeam = new TeamResponse(
                id,
                request.name(),
                request.stadium(),
                request.founded(),
                new ArrayList<>()
        );
        storage.teams.put(id, updatedTeam);
        return updatedTeam;
    }
    public void deleteById(Long id) {
        if (!storage.teams.containsKey(id)) {
            throw new ResourceNotFoundException("Team", id);
        }
        storage.teams.remove(id);
    }
}