package com.example.footballapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "football-exchange";
    public static final String ROUTING_PLAYER_CREATED = "player.created";
    public static final String ROUTING_PLAYER_DELETED = "player.deleted";
    public static final String FOOTBALL_ANALYTICS_FANOUT = "football-analytics-fanout";

    @Bean
    public TopicExchange footballExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange analyticsFanoutExchange() {
        return new FanoutExchange(FOOTBALL_ANALYTICS_FANOUT, true, false);
    }

    @Bean
    @Primary
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());

        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("MESSAGE NOT CONFIRMED: " + cause);
            }
        });

        return template;
    }
}
