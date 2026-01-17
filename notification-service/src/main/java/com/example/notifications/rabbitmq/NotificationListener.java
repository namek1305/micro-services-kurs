package com.example.notifications.rabbitmq;

import com.example.football.events.FootballRatingEvent;
import com.example.notifications.websocket.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationHandler notificationHandler;

    public NotificationListener(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.browser", durable = "true"),
                    exchange = @Exchange(name = "football-analytics-fanout", type = "fanout")
            )
    )
    public void handleFootballRatingEvent(FootballRatingEvent event) {
        log.info("Received football rating event from RabbitMQ: {}", event);

        // Формируем сообщение для клиента (браузера)
        String userMessage = String.format(
                "{\"type\": \"FOOTBALL_RATING\", \"playerId\": %d, \"matchId\": %d, \"score\": %d, \"verdict\": \"%s\"}",
                event.playerId(), event.matchId(), event.score(), event.verdict()
        );

        // Отправляем в браузер
        notificationHandler.broadcast(userMessage);
    }
}
