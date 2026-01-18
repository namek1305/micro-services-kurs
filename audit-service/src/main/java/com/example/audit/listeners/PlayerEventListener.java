package com.example.audit.listeners;

import com.example.football.events.PlayerCreatedEvent;
import com.example.football.events.PlayerDeletedEvent;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerEventListener {

    private static final Logger log = LoggerFactory.getLogger(PlayerEventListener.class);

    private final Set<Long> processedPlayerCreations = ConcurrentHashMap.newKeySet();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "audit-football-queue",
                    durable = "true"
            ),
            exchange = @Exchange(name = "football-exchange", type = "topic", durable = "true"),
            key = "player.created"
    ))
    public void handlePlayerCreated(
            @Payload PlayerCreatedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) throws IOException {

        try {
            if (!processedPlayerCreations.add(event.playerId())) {
                log.warn("⚠ Дубликат PlayerCreatedEvent для playerId={}. Событие игнорируем.", event.playerId());
                channel.basicAck(tag, false);
                return;
            }
            log.info("AUDIT → Создан новый игрок: id={}, name='{}', pos={}",
                    event.playerId(), event.fullName(), event.position());

            channel.basicAck(tag, false);

        } catch (Exception e) {
            log.error("Ошибка обработки PlayerCreatedEvent {}", event, e);
            channel.basicNack(tag, false, false);
        }
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "audit-football-queue",
                    durable = "true"
            ),
            exchange = @Exchange(name = "football-exchange", type = "topic", durable = "true"),
            key = "player.deleted"
    ))
    public void handlePlayerDeleted(
            @Payload PlayerDeletedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) throws IOException {

        try {
            log.info("AUDIT → Игрок удалён: id={}", event.playerId());

            channel.basicAck(tag, false);

        } catch (Exception e) {
            log.error("Ошибка обработки PlayerDeletedEvent {}", event, e);
            channel.basicNack(tag, false, false);
        }
    }
}
