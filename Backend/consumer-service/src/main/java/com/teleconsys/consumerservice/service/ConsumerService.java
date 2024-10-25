package com.teleconsys.consumerservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queues = "employeeQueue")
    public void receiveEmployeeData(Map<String, Object> employeeData) {
        // Elabora e salva i dati su MongoDB
        mongoTemplate.save(employeeData, "employee_logs");
        System.out.println("Received and saved employee data: " + employeeData);
    }
}
