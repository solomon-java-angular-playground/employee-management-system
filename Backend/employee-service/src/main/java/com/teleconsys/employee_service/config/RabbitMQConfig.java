package com.teleconsys.employee_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue employeeRequestQueue() {
        return new Queue("employeeRequestQueue");
    }
}

