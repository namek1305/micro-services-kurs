package com.example.football.events;

import java.io.Serializable;

public record PlayerCreatedEvent(
        long playerId,
        String fullName,
        String position
) implements Serializable {}
