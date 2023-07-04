package com.wcs.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		Random random = new Random();
		SpringApplication.run(SecurityApplication.class, args);
	}

}
