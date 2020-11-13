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
    public void testGetBrand() {
        try {
            weipinhuiDataService.syncGetBrand(1, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void testGetCategory() {
        try {
            weipinhuiDataService.syncGetCategory(1, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void testQueryItemIdList() {
        try {
            weipinhuiDataService.syncItemIdList(1, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Ignore
    @Test
    public void testQueryItemDetail() {
        try {
            weipinhuiDataService.syncItemDetail(1, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void fixStarProperty() {
        try {
            weipinhuiDataService.fixStarProperty();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
