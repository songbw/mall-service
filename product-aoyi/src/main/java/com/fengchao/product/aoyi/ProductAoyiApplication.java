package com.fengchao.product.aoyi;

import com.fengchao.product.aoyi.db.config.DynamicDataSourceConfig;
import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@EnableTaskTracker      // 启动TaskTracker
//@EnableMonitor          // 启动Monitor
@EnableJobClient
@EnableFeignClients
@EnableDiscoveryClient
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan({"com.fengchao.product.aoyi.mapper"})
public class ProductAoyiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAoyiApplication.class, args);
	}

}
