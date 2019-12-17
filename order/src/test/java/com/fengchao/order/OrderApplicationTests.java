package com.fengchao.order;

import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.JSONUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

	@Autowired
	private Environment environment;

//	@Ignore
	@Test
	public void contextLoads() {
		String[] active = environment.getActiveProfiles() ;
		System.out.println(active[0]);

	}

}
