package com.fengchao.elasticsearch;

import com.fengchao.elasticsearch.service.ProductESService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchMallApplicationTests {

	@Autowired
	private ProductESService productESService ;

	@Ignore
	@Test
	public void contextLoads() {
		productESService.delete(20209342) ;
	}

}
