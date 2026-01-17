package com.example.football.events;

import java.io.Serializable;

public record PlayerCreatedEvent(
        Long playerId,
        String fullName,
        String position
) implements Serializable {}
