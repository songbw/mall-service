package com.fengchao.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class ScheduleMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleMallApplication.class, args);
	}

}
