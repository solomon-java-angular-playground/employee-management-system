package com.teleconsys.department_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange employeeExchange() {
        return new DirectExchange("employeeExchange");
    }

    @Bean
    public Queue employeeResponseQueue() {
        return new Queue("employeeResponseQueue");
    }
}

