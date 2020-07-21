package com.fengchao.gateway;

import com.fengchao.gateway.filter.AuthGlobalFilter;
import com.fengchao.gateway.filter.AuthorizeGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayMallApplication.class, args);
	}

	@Bean
	public AuthorizeGatewayFilterFactory gatewayFilterFactory() {
		return new AuthorizeGatewayFilterFactory();
	}

	@Bean
	public AuthGlobalFilter authGlobalFilter() {
		return new AuthGlobalFilter() ;
	}

}
