package com.example.footballapi.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.football.events.FootballRatingEvent;
import com.example.footballapi.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import grpc.demo.AnalyticsServiceGrpc;
import grpc.demo.PlayerRatingRequest;
import grpc.demo.PlayerRatingResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class RatingController {

    private static final Logger log = LoggerFactory.getLogger(RatingController.class);

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;
    private final ObjectMapper objectMapper;

    public RatingController(
            RabbitTemplate rabbitTemplate,
            Tracer tracer,
            ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.tracer = tracer;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/api/players/{id}/rate")
    public String ratePlayer(@PathVariable Long id) {
        try {
            var request = PlayerRatingRequest.newBuilder()
                    .setPlayerId(id)
                    .setMatchId(0L)
                    .build();

            PlayerRatingResponse response = analyticsStub.calculatePlayerRating(request);

            var event = new FootballRatingEvent(
                    response.getPlayerId(),
                    response.getMatchId(),
                    response.getRatingScore(),
                    response.getVerdict()
            );

            Span currentSpan = tracer.currentSpan();
            if (currentSpan == null) {
                log.warn("No active span found — sending message without tracing context");
            }

            MessageProperties props = new MessageProperties();
            props.setContentType("application/json");

            if (currentSpan != null) {
                TraceContext context = currentSpan.context();
                String b3 = context.traceIdString() + "-" + context.spanIdString() + "-1";
                props.setHeader("b3", b3);
                log.debug("Добавлен заголовок трейсинга: b3={}", b3);
            }

            String json = objectMapper.writeValueAsString(event);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), props);

            rabbitTemplate.send(
                    RabbitMQConfig.FOOTBALL_ANALYTICS_FANOUT,
                    "",
                    message
            );

            return "Player football rating = " + response.getRatingScore();

        } catch (Exception e) {
            log.error("Ошибка при расчёте рейтинга", e);
            return "Analytics unavailable → Rating = -1";
        }
    }
}