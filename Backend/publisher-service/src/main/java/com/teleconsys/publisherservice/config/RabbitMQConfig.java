package com.teleconsys.publisherservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
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
