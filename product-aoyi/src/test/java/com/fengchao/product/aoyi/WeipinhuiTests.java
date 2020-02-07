package com.fengchao.product.aoyi;

import com.fengchao.product.aoyi.service.weipinhui.WeipinhuiDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
public class WeipinhuiTests {

	@Autowired
	private WeipinhuiDataService weipinhuiDataService;

	@Test
	@Ignore
	public void testWeipinhui() {
		try {
			weipinhuiDataService.getBrand(2, 1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
