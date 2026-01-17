package com.example.footballapi.controller;

import com.example.football.events.FootballRatingEvent;
import com.example.footballapi.config.RabbitMQConfig;
import grpc.demo.AnalyticsServiceGrpc;
import grpc.demo.PlayerRatingRequest;
import grpc.demo.PlayerRatingResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public RatingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/players/{id}/rate")
    public String ratePlayer(@PathVariable Long id) {

        try {
            var request = PlayerRatingRequest.newBuilder()
                    .setPlayerId(id)
                    // Для примера: matchId = 0 (если нет конкретного матча). Вы можете добавить параметр в запрос.
                    .setMatchId(0L)
                    .build();

            PlayerRatingResponse response = analyticsStub.calculatePlayerRating(request);

            var event = new FootballRatingEvent(
                    response.getPlayerId(),
                    response.getMatchId(),
                    response.getRatingScore(),
                    response.getVerdict()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.FOOTBALL_ANALYTICS_FANOUT,
                    "",
                    event
            );

            return "Player football rating = " + response.getRatingScore();

        } catch (Exception e) {
            return "Analytics unavailable → Rating = -1";
        }
    }
}
