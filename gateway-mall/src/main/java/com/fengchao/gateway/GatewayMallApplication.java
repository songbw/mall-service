package com.fengchao.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayMallApplication.class, args);
	}

}
