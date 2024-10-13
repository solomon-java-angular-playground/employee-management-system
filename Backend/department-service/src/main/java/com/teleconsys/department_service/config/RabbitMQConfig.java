package com.teleconsys.department_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue departmentQueue() {
        return new Queue("departmentQueue", false);
    }

    @Bean
    public TopicExchange departmentExchange() {
        return new TopicExchange("departmentExchange");
    }

    @Bean
    public Binding binding(Queue departmentQueue, TopicExchange departmentExchange) {
        return BindingBuilder.bind(departmentQueue).to(departmentExchange).with("department.routingkey");
    }
}
