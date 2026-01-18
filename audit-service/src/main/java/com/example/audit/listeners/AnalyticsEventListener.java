package com.example.audit.listeners;

import com.example.football.events.FootballRatingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class AnalyticsEventListener {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.audit.analytics", durable = "true"),
                    exchange = @Exchange(name = "football-analytics-fanout", type = "fanout")
            ),
            ackMode = "MANUAL"
    )
    public void handle(
            @Payload FootballRatingEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {
        try {
            log.info("AUDIT → Player {} got football rating {} (match {}) — verdict={}",
                    event.playerId(), event.score(), event.matchId(), event.verdict());

            channel.basicAck(tag, false);

        } catch (Exception e) {
            log.error("Ошибка обработки FootballRatingEvent: {}", event, e);
            try {
                channel.basicNack(tag, false, true);
            } catch (Exception ex) {
                log.error("Не удалось отправить NACK", ex);
            }
        }
    }
}