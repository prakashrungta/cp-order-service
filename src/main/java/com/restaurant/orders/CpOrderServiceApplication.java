package com.restaurant.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CpOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CpOrderServiceApplication.class, args);
	}

}
