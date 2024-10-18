package com.teleconsys.department_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");  // Permette di deserializzare tutte le classi
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());  // Assicura che anche i listener usino JSON
        return factory;
    }

    // Exchange per le richieste di creazione del dipartimento
    @Bean
    public DirectExchange departmentExchange() {
        return new DirectExchange("departmentExchange");
    }

    // Coda per le richieste di creazione del dipartimento
    @Bean
    public Queue departmentRequestQueue() {
        return new Queue("departmentRequestQueue");
    }

    // Binding tra exchange e coda per le richieste di creazione del dipartimento
    @Bean
    public Binding departmentBinding(Queue departmentRequestQueue, DirectExchange departmentExchange) {
        return BindingBuilder.bind(departmentRequestQueue).to(departmentExchange).with("department.create");
    }

    @Bean
    public DirectExchange employeeExchange() {
        return new DirectExchange("employeeExchange");
    }

    @Bean
    public Queue employeeResponseQueue() {
        return new Queue("employeeResponseQueue");
    }

    // Il binding collega l'exchange alla coda con la routing key
    @Bean
    public Binding binding(DirectExchange employeeExchange, Queue employeeResponseQueue) {
        return BindingBuilder.bind(employeeResponseQueue).to(employeeExchange).with("employee.routingkey");
    }
}
