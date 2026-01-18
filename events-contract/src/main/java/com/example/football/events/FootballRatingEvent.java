package com.example.football.events;

import java.io.Serializable;


public record FootballRatingEvent(
        long playerId,
        long matchId,
        int score,
        String verdict
) implements Serializable {}