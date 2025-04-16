package com.ozmenyavuz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan(basePackages = {"com.ozmenyavuz"})
@SpringBootApplication
@ComponentScan("com.ozmenyavuz")
public class Kafka101Application {

	public static void main(String[] args) {
		SpringApplication.run(Kafka101Application.class, args);
	}

}
