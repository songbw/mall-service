package com.fengchao.product.aoyi.service.weipinhui;

import com.fengchao.product.aoyi.dao.AoyiBaseBrandDao;
import com.fengchao.product.aoyi.dao.CategoryDao;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiSkuResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private CategoryDao categoryDao;

    public WeipinhuiDataServiceImpl(AoyiClientRpcService aoyiClientRpcService,
                                    AoyiBaseBrandDao aoyiBaseBrandDao,
                                    CategoryDao categoryDao) {
        this.aoyiClientRpcService = aoyiClientRpcService;
        this.aoyiBaseBrandDao = aoyiBaseBrandDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public void syncGetBrand(Integer pageNumber, Integer maxPageCount) throws Exception {
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
                        // 查询数据库是否已经存在该brand
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


    @Override
    public void syncGetCategory(Integer pageNumber, Integer maxPageCount) throws Exception {
        try {
            int pageCount = 0;
            int totalInsert = 0; // 记录一下本次执行一共插入的数据数量


            while (true) {
                // 1. 获取数据
                List<CategoryResDto> categoryResDtoList = aoyiClientRpcService.weipinhuiGetCategory(pageNumber, PAGESIZE);

                log.info("同步品类 第{}页 共{}条数据 >>>> {}",
                        pageNumber, categoryResDtoList.size(), JSONUtil.toJsonString(categoryResDtoList));

                // 2. 将数据转map key:categoryId value:AoyiBaseCategory
                Map<Integer, AoyiBaseCategory> aoyiBaseCategoryMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(categoryResDtoList)) {
                    for (CategoryResDto categoryResDto : categoryResDtoList) {
                        Integer idLevel1 = Integer.valueOf(categoryResDto.getCategoryIdLevel1());
                        Integer idLevel2 = Integer.valueOf(categoryResDto.getCategoryIdLevel2());
                        Integer idLevel3 = Integer.valueOf(categoryResDto.getCategoryIdLevel3());

                        // 处理level3
                        if (aoyiBaseCategoryMap.get(idLevel3) == null) {
                            AoyiBaseCategory category = new AoyiBaseCategory();
                            category.setCategoryId(idLevel3);
                            category.setCategoryName(categoryResDto.getCategoryNameLevel3());
                            category.setParentId(idLevel2);
                            category.setCategoryClass("3");
                            category.setCategoryDesc("唯品会");
                            category.setIdate(new Date());

                            aoyiBaseCategoryMap.put(idLevel3, category);
                        }

                        // 处理level2
                        if (aoyiBaseCategoryMap.get(idLevel2) == null) {
                            AoyiBaseCategory category = new AoyiBaseCategory();
                            category.setCategoryId(idLevel2);
                            category.setCategoryName(categoryResDto.getCategoryNameLevel2());
                            category.setParentId(idLevel1);
                            category.setCategoryClass("2");
                            category.setCategoryDesc("唯品会");
                            category.setIdate(new Date());

                            aoyiBaseCategoryMap.put(idLevel2, category);
                        }

                        // 处理level1
                        if (aoyiBaseCategoryMap.get(idLevel1) == null) {
                            AoyiBaseCategory category = new AoyiBaseCategory();
                            category.setCategoryId(idLevel1);
                            category.setCategoryName(categoryResDto.getCategoryNameLevel1());
                            category.setParentId(0);
                            category.setCategoryClass("1");
                            category.setCategoryDesc("唯品会");
                            category.setIdate(new Date());

                            aoyiBaseCategoryMap.put(idLevel1, category);
                        }
                    } // end for

                    log.info("同步品类 生成品类数据:{}条, Map<Integer, AoyiBaseCategory>: {}",
                            aoyiBaseCategoryMap.size(), JSONUtil.toJsonString(aoyiBaseCategoryMap));

                    // 3. 提取出上一步map的key集合，然后查询是否有需要插入的数据
                    List<Integer> categoryIdList = new ArrayList<>(aoyiBaseCategoryMap.keySet());
                    // 查询
                    List<AoyiBaseCategory> aoyiBaseCategoryList = categoryDao.selectByCategoryIdList(categoryIdList);
                    for (AoyiBaseCategory aoyiBaseCategory : aoyiBaseCategoryList) {
                        if (aoyiBaseCategoryMap.get(aoyiBaseCategory.getCategoryId()) != null) {
                            aoyiBaseCategoryMap.remove(aoyiBaseCategory.getCategoryId());
                        }
                    }

                    log.info("同步品类 第{}页 共找到需要同步的数据{}条 数据是: {}",
                            pageNumber, aoyiBaseCategoryMap.size(), JSONUtil.toJsonString(aoyiBaseCategoryMap));

                    // 4. 批量插入数据库
                    if (aoyiBaseCategoryMap.size() > 0) {
                        // 执行插入
                        categoryDao.batchInsert(new ArrayList<>(aoyiBaseCategoryMap.values()));
                    }

                    totalInsert = totalInsert + aoyiBaseCategoryMap.size();
                } // end fi 如果分页查询到数据

                log.info("同步品类 第{}页 累计插入数据{}条", pageNumber, totalInsert);

                // 3. 判断是否需要继续同步
                if (categoryResDtoList.size() == 0) {
                    log.info("同步品类 结束");
                    break;
                }

                pageNumber++;
                pageCount++;

                //
                if (maxPageCount != -1 && pageCount >= maxPageCount) {
                    log.warn("同步品类 达到最大页数{}限制 停止同步!", maxPageCount);

                    break;
                }
            } // end while
        } catch (Exception e) {
            log.error("同步品类 异常:{}", e.getMessage(), e);
        }
    }

    @Override
    public void syncItemIdList(Integer pageNumber, Integer maxPageCount) throws Exception {
        try {
            int pageCount = 0;
            int totalInsert = 0; // 记录一下执行一共插入的数据数量


            while (true) {
                // 1. 获取数据
                List<AoyiItemDetailResDto> aoyiItemDetailResDtoList
                        = aoyiClientRpcService.weipinhuiQueryItemsList(pageNumber, PAGESIZE);

                log.info("同步itemIdList 第{}页 共{}条数据 >>>> {}",
                        pageNumber, aoyiItemDetailResDtoList.size(), JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDtoList));

                // 2. 入库处理
                if (CollectionUtils.isNotEmpty(aoyiItemDetailResDtoList)) {
                    for (AoyiItemDetailResDto aoyiItemDetailResDto : aoyiItemDetailResDtoList) {

                    }



                    // 执行插入
                    // aoyiBaseBrandDao.batchInsert(insertAoyiBaseBrandList);

                    totalInsert = totalInsert + aoyiItemDetailResDtoList.size();
                }

                // 3. 判断是否需要继续同步
                if (aoyiItemDetailResDtoList.size() == 0) {
                    log.info("同步itemIdList 结束");
                    break;
                }

                log.info("同步itemIdList 第{}页 累计插入数据{}条", pageNumber, totalInsert);

                pageNumber++;
                pageCount++;

                //
                if (maxPageCount != -1 && pageCount >= maxPageCount) {
                    log.warn("同步itemIdList 达到最大页数{}限制 停止同步!", maxPageCount);

                    break;
                }


            } // end while
        } catch (Exception e) {
            log.error("同步品牌 异常:{}", e.getMessage(), e);
        }
    }

    /**
     * {
     * "code": 200,
     * "msg": "Success",
     * "data": {
     * "itemId": "30007552",
     * "itemTitle": "南极人（10双装）条纹星星 时尚透气 船袜 女士 袜子",
     * "canSell": "false",
     * "brandId": "1597",
     * "categoryId": "11233031",
     * "itemImage": "[\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/1.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/2.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/3.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/4.jpg\"]",
     * "itemDetailImage": "[\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/7.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/8.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/9.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/10.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/11.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/12.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/13.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/14.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/15.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/16.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/17.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/18.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/19.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/20.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/21.jpg\"]",
     * "aoyiSkusResponses": [
     * {
     * "skuId": "30012086",
     * "sepca": "混色",
     * "sepcb": null,
     * "sepcc": null,
     * "priceCent": "43.47",
     * "sellPrice": "49",
     * "canSell": "false",
     * "skuImageUrl": "/aoyi_vip/aoyi30010k/30007552/ZT/30012086/5.jpg",
     * "skuImage": "[\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/1.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/2.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/3.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/4.jpg\"]",
     * "skuDetailImage": "[\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/7.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/8.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/9.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/10.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/11.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/12.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/13.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/14.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/15.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/16.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/17.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/18.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/19.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/20.jpg\",\"/aoyi_vip/aoyi30010k/30007552/ZT/30012086/21.jpg\"]"
     * }
     * ]
     * }
     * }
     *
     * @param itemIdList
     * @throws Exception
     */
    @Override
    public void syncItemDetail(List<String> itemIdList, Integer maxCount) throws Exception {
        try {
            int totalSku = 0;
            int totalItem = 0; // 记录一下本次执行一共插入的数据数量

            for (String itemId : itemIdList) {
                // 1. 获取数据
                AoyiItemDetailResDto aoyiItemDetailResDto = aoyiClientRpcService.weipinhuiQueryItemDetial(itemId);
                log.info("同步商品itemId:{}  >>>>>> AoyiItemDetailResDto: {}",
                        itemId, JSONUtil.toJsonString(aoyiItemDetailResDto));

                // 2. 入库处理
                int _tmpSkuCount = 0;
                if (aoyiItemDetailResDto != null) {
                    // 获取sku集合
                    List<AoyiSkuResDto> aoyiSkuResDtoList = aoyiItemDetailResDto.getAoyiSkusResponses();

                    _tmpSkuCount = (aoyiSkuResDtoList == null ? 0 : aoyiSkuResDtoList.size());
                    log.info("同步商品itemId:{} 获取到sku {}个",
                            itemId, aoyiSkuResDtoList == null ? null : aoyiSkuResDtoList.size());

                    if (CollectionUtils.isNotEmpty(aoyiSkuResDtoList)) {
                        // 遍历sku
                        for (AoyiSkuResDto aoyiSkuResDto : aoyiSkuResDtoList) {

                        }

                        // 执行插入
                        // aoyiBaseBrandDao.batchInsert(insertAoyiBaseBrandList);
                    }
                }

                totalSku = totalSku + _tmpSkuCount;
                totalItem++;

                log.info("同步商品itemId:{} <<<<<< 累计插入spu:{} 条 sku:{}条", itemId, totalItem, totalSku);

                // 3. 判断是否需要继续同步
                if (maxCount != -1 && totalItem >= maxCount) {
                    log.info("同步商品itemId:{} 达到最大限制:{} 同步结束", itemId, maxCount);
                    break;
                }
            } // end for
        } catch (Exception e) {
            log.error("同步商品 异常:{}", e.getMessage(), e);
        }
    }
}
