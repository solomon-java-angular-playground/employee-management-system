package com.teleconsys.publisherservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Sintassi cron: secondi minuti ore giorno-del-mese mese giorno-della-settimana.
    // */numero significa ogni X unità
    // es. ogni 5 minuti: @Scheduled(cron = "0 0/5 * * * ?") // Schedula ogni 5 minuti
    @Scheduled(cron = "*/5 * * * * ?") // Schedula ogni 5 secondi
    public void publishEmployeeData() {
        Map<String, Object> employeeData = generateEmployeeData();
        rabbitTemplate.convertAndSend("employeeExchange", "employee.routingkey", employeeData);
        System.out.println("Published employee data to queue: " + employeeData);
    }

    private Map<String, Object> generateEmployeeData() {
        // Simula i dati dell'impiegato da scodare
        return Map.of(
                "employeeId", UUID.randomUUID().toString(),
                "name", "John Doe",
                "department", "HR",
                "email", "john.doe@example.com",
                "action", "created"
        );
    }
}
