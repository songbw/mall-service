package com.fengchao.order;

import com.fengchao.order.service.OrderService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

	@Autowired
	private OrderService orderService ;

	@Ignore
	@Test
	public void contextLoads() {
		orderService.unpaid("11DFDBF1C25AB@EF6E2A7@AEM1L5D6GBD2") ;
	}

}
