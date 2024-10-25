package com.teleconsys.publisherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PublisherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublisherServiceApplication.class, args);
	}

}
