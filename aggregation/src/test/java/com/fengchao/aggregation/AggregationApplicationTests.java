package com.fengchao.aggregation;

import com.fengchao.aggregation.service.AggregationService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AggregationApplicationTests {

    @Autowired
    private AggregationService aggregationService ;

    @Ignore
    @Test
    public void contextLoads() {
        aggregationService.updateMpuPriceAndStateForAggregationBatch();
    }

}
