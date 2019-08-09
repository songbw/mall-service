package com.fengchao.equity;

import com.fengchao.equity.db.config.DynamicDataSourceConfig;
import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableTaskTracker      // 启动TaskTracker
//@EnableMonitor          // 启动Monitor
@EnableJobClient
@EnableFeignClients
@EnableDiscoveryClient
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan({"com.fengchao.equity.mapper"})
public class EquityApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquityApplication.class, args);
    }

    @PostConstruct
    void started() { TimeZone.setDefault(TimeZone.getTimeZone("UTC"));}
}
