package com.example.audit.listeners;

import com.example.football.events.FootballRatingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventListener {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.audit.analytics", durable = "true"),
                    exchange = @Exchange(name = "football-analytics-fanout", type = "fanout")
            )
    )
    public void handle(FootballRatingEvent event) {
        log.info("AUDIT → Player {} got football rating {} (match {}) — verdict={}",
                event.playerId(), event.score(), event.matchId(), event.verdict());
    }
}
