package com.example.footballapi.controller;

import com.example.footballapi.api.dto.TeamRequest;
import com.example.footballapi.api.dto.TeamResponse;
import com.example.footballapi.api.endpoints.TeamApi;
import com.example.footballapi.assemblers.TeamModelAssembler;
import com.example.footballapi.service.TeamService;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class TeamController implements TeamApi {

    private final TeamService teamService;
    private final TeamModelAssembler assembler;

    public TeamController(TeamService teamService, TeamModelAssembler assembler) {
        this.teamService = teamService;
        this.assembler = assembler;
    }

    @Override
    public List<TeamResponse> getAllTeams() {
        return teamService.findAll();
    }

    @GetMapping("/api/teams/hateoas")
    public CollectionModel<EntityModel<TeamResponse>> getAllTeamsHateoas() {
        List<TeamResponse> teams = teamService.findAll();
        return assembler.toCollectionModel(teams);
    }

    @Override
    public TeamResponse getTeamById(Long id) {
        return teamService.findById(id);
    }

    @GetMapping("/api/teams/{id}/hateoas")
    public EntityModel<TeamResponse> getTeamByIdHateoas(@PathVariable Long id) {
        TeamResponse team = teamService.findById(id);
        return assembler.toModel(team);
    }

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        return teamService.create(request);
    }

    @Override
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        return teamService.update(id, request);
    }

    @Override
    public void deleteTeam(Long id) {
        teamService.deleteById(id);
    }
}
