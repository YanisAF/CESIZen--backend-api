package com.example.CESIZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CesiZenApplication {
	public static void main(String[] args) {
        SpringApplication.run(CesiZenApplication.class, args);
	}
}
