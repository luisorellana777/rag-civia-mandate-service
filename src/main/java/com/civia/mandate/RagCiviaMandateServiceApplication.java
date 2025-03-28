package com.civia.mandate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class RagCiviaMandateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagCiviaMandateServiceApplication.class, args);
	}

}
