package com.fengchao.product.aoyi.service.weipinhui;

import com.fengchao.product.aoyi.dao.AoyiBaseBrandDao;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于同步唯品会数据的服务
 */
@Slf4j
@Service
public class WeipinhuiDataServiceImpl implements WeipinhuiDataService {

    private static final Integer PAGESIZE = 20;

    private AoyiClientRpcService aoyiClientRpcService;

    private AoyiBaseBrandDao aoyiBaseBrandDao;

    public WeipinhuiDataServiceImpl(AoyiClientRpcService aoyiClientRpcService,
                                    AoyiBaseBrandDao aoyiBaseBrandDao) {
        this.aoyiClientRpcService = aoyiClientRpcService;
        this.aoyiBaseBrandDao = aoyiBaseBrandDao;
    }

    @Override
    public void getBrand(Integer pageNumber, Integer maxPageCount) throws Exception {
        try {
            int pageCount = 0;
            int totalInsert = 0; // 记录一下本次执行一共插入的数据数量


            while (true) {
                // 1. 获取数据
                List<BrandResDto> brandResDtoList = aoyiClientRpcService.weipinhuiGetBrand(pageNumber, PAGESIZE);

                List<String> brandIdList = brandResDtoList.stream().map(b -> b.getBrandId()).collect(Collectors.toList());
                log.info("同步品牌 第{}页 共{}条数据 >>>> {}",
                        pageNumber, brandResDtoList.size(), JSONUtil.toJsonString(brandIdList));

                // 2. 入库处理
                List<Integer> newBrandIdList = new ArrayList<>(); // 记录一下插入的brand(已有的不需要插入)
                List<AoyiBaseBrand> insertAoyiBaseBrandList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(brandResDtoList)) {
                    for (BrandResDto brandResDto : brandResDtoList) {
                        // 查询数据库是否已经存在改brand
                        Integer brandId = Integer.valueOf(brandResDto.getBrandId());
                        AoyiBaseBrand exsitAoyiBaseBrand = aoyiBaseBrandDao.selectByBrandId(brandId);

                        // 如果不存在, 则需要插入
                        if (exsitAoyiBaseBrand == null) {
                            AoyiBaseBrand insertAoyiBaseBrand = new AoyiBaseBrand();
                            insertAoyiBaseBrand.setBrandId(brandId);
                            insertAoyiBaseBrand.setBrandName(brandResDto.getBrandName());
                            insertAoyiBaseBrand.setBrandDesc("唯品会");

                            newBrandIdList.add(brandId);
                            insertAoyiBaseBrandList.add(insertAoyiBaseBrand);
                        }

                    }

                    log.info("同步品牌 第{}页 需要插入数据{}条: {}",
                            pageNumber, newBrandIdList.size(), JSONUtil.toJsonString(newBrandIdList));

                    // 执行插入
                    // aoyiBaseBrandDao.batchInsert(insertAoyiBaseBrandList);

                    totalInsert = totalInsert + newBrandIdList.size();
                }

                // 3. 判断是否需要继续同步
                if (brandResDtoList.size() == 0) {
                    log.info("同步品牌 结束");
                    break;
                }

                pageNumber++;
                pageCount++;

                //
                if (maxPageCount != -1 && pageCount >= maxPageCount) {
                    log.warn("同步品牌 达到最大页数{}限制 停止同步!", maxPageCount);

                    break;
                }

                log.info("同步品牌 累计插入数据{}条", totalInsert);
            } // end while
        } catch (Exception e) {
            log.error("同步品牌 异常:{}", e.getMessage(), e);
        }

    }
}
