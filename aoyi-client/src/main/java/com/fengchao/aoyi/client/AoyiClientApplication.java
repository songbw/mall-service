package com.fengchao.aoyi.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AoyiClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AoyiClientApplication.class, args);
	}

}
