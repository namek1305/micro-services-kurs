package com.example.footballapi.service;

import com.example.football.events.PlayerCreatedEvent;
import com.example.football.events.PlayerDeletedEvent;
import com.example.footballapi.api.dto.PlayerRequest;
import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.api.exception.ResourceNotFoundException;
import com.example.footballapi.config.RabbitMQConfig;
import com.example.footballapi.storage.InMemoryStorage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final InMemoryStorage storage;
    private final TeamService teamService;
    private final RabbitTemplate rabbitTemplate;

    public PlayerService(InMemoryStorage storage, @Lazy TeamService teamService, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.teamService = teamService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<PlayerResponse> findAll() {
        return new ArrayList<>(storage.players.values());
    }

    public PlayerResponse findById(Long id) {
        return Optional.ofNullable(storage.players.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Player", id));
    }

    public PlayerResponse create(PlayerRequest request) {
        Long id = storage.playerSequence.incrementAndGet();
        PlayerResponse player = new PlayerResponse(
                id,
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.position()
        );
        storage.players.put(id, player);

        PlayerCreatedEvent event = new PlayerCreatedEvent(
                player.getId(),
                player.getFirstName() + " " + player.getLastName(),
                player.getPosition()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_PLAYER_CREATED,
                event
        );

        return player;
    }

    public PlayerResponse update(Long id, PlayerRequest request) {
        findById(id);
        PlayerResponse updatedPlayer = new PlayerResponse(
                id,
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.position()
        );
        storage.players.put(id, updatedPlayer);
        return updatedPlayer;
    }

    public void deleteById(Long id) {
        PlayerResponse player = findById(id);
        storage.players.remove(id);

        PlayerDeletedEvent event = new PlayerDeletedEvent(player.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_PLAYER_DELETED,
                event
        );
    }
}