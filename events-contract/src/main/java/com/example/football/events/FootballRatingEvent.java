package com.example.football.events;

import java.io.Serializable;


public record FootballRatingEvent(
        Long playerId,
        Long matchId,
        Integer score,
        String verdict
) implements Serializable {}