package com.project.ticketmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TicketMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketMicroserviceApplication.class, args);
	}

}
