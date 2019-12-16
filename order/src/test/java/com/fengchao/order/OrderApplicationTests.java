package com.fengchao.order;

import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.JSONUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

	@Autowired
	private OrderService orderService ;

//	@Ignore
	@Test
	public void contextLoads() {
		List<Integer>  all = new ArrayList<>() ;
		List<Integer> sub = new ArrayList<>() ;
		for (int i = 0; i < 50; i++) {
			all.add(i) ;
		}
		for (int i = 0; i < 50; i++) {
			if (i+11 > 50) {
				sub = all.subList(i, 50);
			} else {
				sub = all.subList(i, i + 11) ;
			}
			System.out.println(i);
			i = i + 10 ;

			System.out.println(JSONUtil.toJsonString(sub));
		}
	}

}
