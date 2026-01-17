package com.example.football.events;

import java.io.Serializable;

public record PlayerDeletedEvent(Long playerId) implements Serializable {}
