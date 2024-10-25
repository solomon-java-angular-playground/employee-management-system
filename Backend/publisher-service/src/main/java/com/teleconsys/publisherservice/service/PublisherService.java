package com.teleconsys.publisherservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class PublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Random random = new Random();

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
        String[] names = {"Pino Pinotti", "Marco Marconi", "Mario Rossi", "Aubrey Graham"};
        String[] departments = {"HR", "Sales", "IT", "Administration"};
        String[] emails = {"example1@example.com", "example2@example.com", "example3@example.com", "example4@example.com"};
        String[] actions = {"created", "updated", "deleted"};

        return Map.of(
                "employeeId", UUID.randomUUID().toString(),
                "name", names[random.nextInt(names.length)],
                "department", departments[random.nextInt(departments.length)],
                "email", emails[random.nextInt(emails.length)],
                "action", actions[random.nextInt(actions.length)]
        );
    }
}
