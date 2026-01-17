package com.example.footballapi.controller;

import com.example.footballapi.api.dto.PlayerRequest;
import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.api.endpoints.PlayerApi;
import com.example.footballapi.assemblers.PlayerModelAssembler;
import com.example.footballapi.service.PlayerService;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PlayerController implements PlayerApi {

    private final PlayerService playerService;
    private final PlayerModelAssembler assembler;

    public PlayerController(PlayerService playerService, PlayerModelAssembler assembler) {
        this.playerService = playerService;
        this.assembler = assembler;
    }

    @Override
    public List<PlayerResponse> getAllPlayers() {
        List<PlayerResponse> players = playerService.findAll();
        return players;
    }

    @GetMapping("/api/players/hateoas")
    public CollectionModel<EntityModel<PlayerResponse>> getAllPlayersHateoas() {
        List<PlayerResponse> players = playerService.findAll();
        return assembler.toCollectionModel(players);
    }

    @Override
    public PlayerResponse getPlayerById(Long id) {
        return playerService.findById(id);
    }

    @GetMapping("/api/players/{id}/hateoas")
    public EntityModel<PlayerResponse> getPlayerByIdHateoas(@PathVariable Long id) {
        PlayerResponse player = playerService.findById(id);
        return assembler.toModel(player);
    }

    @Override
    public PlayerResponse createPlayer(PlayerRequest request) {
        return playerService.create(request);
    }

    @Override
    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        return playerService.update(id, request);
    }

    @Override
    public void deletePlayer(Long id) {
        playerService.deleteById(id);
    }
}
