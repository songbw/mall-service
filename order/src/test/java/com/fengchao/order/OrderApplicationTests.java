package com.fengchao.order;

import com.fengchao.order.bean.InventoryMpus;
import com.fengchao.order.dao.InventoryDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

	@Autowired
	private InventoryDao inventoryDao ;

	@Test
	public void contextLoads() {
		InventoryMpus inventoryMpus = new InventoryMpus() ;
		inventoryMpus.setMpu("99000128");
		inventoryMpus.setRemainNum(1);
		inventoryDao.inventorySub(inventoryMpus) ;
	}

}
