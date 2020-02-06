package com.fengchao.product.aoyi.service.weipinhui;

import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用于同步唯品会数据的服务
 */
@Slf4j
@Service
public class WeipinhuiDataServiceImpl implements WeipinhuiDataService {

    private static final Integer PAGESIZE = 20;

    private AoyiClientRpcService aoyiClientRpcService;

    public WeipinhuiDataServiceImpl(AoyiClientRpcService aoyiClientRpcService) {
        this.aoyiClientRpcService = aoyiClientRpcService;
    }

    @Override
    public void getBrand(Integer pageNumber, Integer maxPageCount) throws Exception {
        try {
            int pageCount = 0;

            while (true) {
                // 1. 获取数据
                List<BrandResDto> brandResDtoList = aoyiClientRpcService.weipinhuiGetBrand(pageNumber, PAGESIZE);
                log.info("同步品牌 第{}页 共{}条数据>>>>", pageNumber, brandResDtoList.size());

                // 2. 入库处理
                if (CollectionUtils.isNotEmpty(brandResDtoList)) {
                    for (BrandResDto brandResDto : brandResDtoList) {

                    }
                }

                // 3. 判断是否需要继续同步
                if (brandResDtoList.size() <= PAGESIZE) {
                    log.info("同步品牌 ");
                    break;
                }

                pageNumber++;
                pageCount++;

                //
                if (maxPageCount != -1 && pageCount >= maxPageCount) {
                    log.warn("同步品牌 达到最大页数{}限制 停止同步!", maxPageCount);
                }
            }
        } catch (Exception e) {
            log.error("");
        }

    }
}
