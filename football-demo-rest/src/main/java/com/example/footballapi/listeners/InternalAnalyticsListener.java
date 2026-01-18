package com.example.footballapi.listeners;

import com.example.football.events.FootballRatingEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InternalAnalyticsListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.demorest.analytics.log", durable = "true"),
                    exchange = @Exchange(name = "football-analytics-fanout", type = "fanout")
            )
    )
    public void handle(FootballRatingEvent event) {
        System.out.println("DEMO-REST RECEIVED FOOTBALL RATING: Player " + event.playerId()
                + " match=" + event.matchId() + " score=" + event.score());
    }
}
