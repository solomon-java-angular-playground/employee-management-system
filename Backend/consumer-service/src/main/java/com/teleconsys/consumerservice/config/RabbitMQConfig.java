package com.teleconsys.consumerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Conversione dei messaggi in JSON per non avere problemi di
    // deserializzazione di classi sconosciute
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange employeeExchange() {
        return new DirectExchange("employeeExchange");
    }

    @Bean
    public Queue employeeQueue() {
        return new Queue("employeeQueue");
    }

    @Bean
    public Binding binding(Queue employeeQueue, DirectExchange employeeExchange) {
        return BindingBuilder.bind(employeeQueue).to(employeeExchange).with("employee.routingkey");
    }
}
