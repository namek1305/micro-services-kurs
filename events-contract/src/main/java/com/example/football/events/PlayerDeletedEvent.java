package com.example.football.events;

import java.io.Serializable;

public record PlayerDeletedEvent(
        long playerId
)
        implements Serializable {}
