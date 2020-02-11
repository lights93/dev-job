package com.mino.devjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevjobApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevjobApplication.class, args);
	}

}
