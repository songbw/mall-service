package com.fengchao.product.aoyi;

import com.alibaba.fastjson.JSON;
import com.fengchao.product.aoyi.service.weipinhui.WeipinhuiDataService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
public class WeipinhuiTests {

    @Autowired
    private WeipinhuiDataService weipinhuiDataService;

    @Test
    @Ignore
    public void testGetBrand() {
        try {
            weipinhuiDataService.syncGetBrand(2, 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void testGetCategory() {
        try {
            weipinhuiDataService.syncGetCategory(1, 2);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void testQueryItemIdList() {
        try {
            weipinhuiDataService.syncItemIdList(1, 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @Test
    public void testQueryItemDetail() {
        try {
            weipinhuiDataService.syncItemDetail(1, 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }
}
