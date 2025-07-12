package com.github.pfichtner.httpwithspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BerechtigungenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BerechtigungenApplication.class, args);
	}

}
