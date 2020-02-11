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
    public void testQueryItemDetail() {
        try {
            String itemIds = "[\"30007551\",\"30007552\",\"30007554\",\"30007560\",\"30007561\",\"30007567\",\"30007569\",\"30007580\",\"30007581\",\"30007586\",\"30007589\",\"30007594\",\"30007595\",\"30008149\",\"30008295\",\"30008302\",\"30008335\",\"30008458\",\"30008594\",\"30008601\",\"30008604\",\"30008607\",\"30008993\",\"30009002\",\"30009004\",\"30011419\",\"30011425\",\"30011427\",\"30011428\",\"30011449\",\"30011479\",\"30011486\",\"30011487\",\"30011494\",\"30011495\",\"30011543\",\"30011561\",\"30011595\",\"30011607\",\"30011608\",\"30011622\",\"30011660\",\"30011671\",\"30011680\",\"30011759\",\"30011763\",\"30011766\"]";

            List<String> itemIdList = JSON.parseArray(itemIds, String.class);

            weipinhuiDataService.syncItemDetail(itemIdList, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }
}
