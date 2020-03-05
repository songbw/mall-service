package com.fengchao.product.aoyi.service.weipinhui;

import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.config.ProductConfig;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiSkuResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.fengchao.product.aoyi.utils.PriceUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于同步唯品会数据的服务
 */
@Slf4j
@Service
public class WeipinhuiDataServiceImpl implements WeipinhuiDataService {

    /**
     * 主图
     */
    private static final String IMAGE_TYPE_ZT = "WEIPINHUI_ZT";

    /**
     * 详情
     */
    private static final String IMAGE_TYPE_XQ = "WEIPINHUI_XQ";

    private static final Integer PAGESIZE = 20;

    private static final String MERCHANT_CODE = "30";

    private ProductConfig productConfig;

    private AoyiClientRpcService aoyiClientRpcService;

    private AoyiBaseBrandDao aoyiBaseBrandDao;

    private CategoryDao categoryDao;

    private ProductDao productDao;

    private StarSkuDao starSkuDao;

    private StarPropertyDao starPropertyDao;

    private AyFcImagesDao ayFcImagesDao;

    public WeipinhuiDataServiceImpl(AoyiClientRpcService aoyiClientRpcService,
                                    AoyiBaseBrandDao aoyiBaseBrandDao,
                                    CategoryDao categoryDao,
                                    ProductDao productDao,
                                    StarSkuDao starSkuDao,
                                    StarPropertyDao starPropertyDao,
                                    ProductConfig productConfig,
                                    AyFcImagesDao ayFcImagesDao) {
        this.aoyiClientRpcService = aoyiClientRpcService;
        this.aoyiBaseBrandDao = aoyiBaseBrandDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.starSkuDao = starSkuDao;
        this.starPropertyDao = starPropertyDao;
        this.productConfig = productConfig;
        this.ayFcImagesDao = ayFcImagesDao;
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
                    if (CollectionUtils.isNotEmpty(insertAoyiBaseBrandList)) {
                        aoyiBaseBrandDao.batchInsert(insertAoyiBaseBrandList);
                    }

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

                log.info("同步itemIdList 第{}页 共查询到{}条数据 >>>> {}",
                        pageNumber, aoyiItemDetailResDtoList.size(), JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDtoList));

                // 2. 入库处理
                if (CollectionUtils.isNotEmpty(aoyiItemDetailResDtoList)) {
                    // 转数据库实体
                    List<AoyiProdIndex> aoyiProdIndexList = new ArrayList<>();
                    List<String> itemIdList = new ArrayList<>();
                    for (AoyiItemDetailResDto aoyiItemDetailResDto : aoyiItemDetailResDtoList) {
                        AoyiProdIndex aoyiProdIndex = new AoyiProdIndex();

                        aoyiProdIndex.setSkuid(aoyiItemDetailResDto.getItemId());
                        aoyiProdIndex.setMpu(aoyiItemDetailResDto.getItemId());
                        aoyiProdIndex.setMerchantId(2);
                        aoyiProdIndex.setState(String.valueOf(ProductStatusEnum.PUT_ON.getValue())); // 下架状态

                        itemIdList.add(aoyiItemDetailResDto.getItemId());
                        aoyiProdIndexList.add(aoyiProdIndex);
                    }

                    // 查询数据库是否已存在数据
                    List<AoyiProdIndex> exsitAoyiprodIndexList =
                            productDao.selectAoyiProdIndexListByMpuIdList(itemIdList);

                    List<String> exsitItemIdList =
                            exsitAoyiprodIndexList.stream().map(e -> e.getSkuid()).collect(Collectors.toList());

                    // 去掉已存在的数据
                    List<AoyiProdIndex> insertAoyiProdIndexList = new ArrayList<>(); // 准备插入的数据
                    for (AoyiProdIndex aoyiProdIndex : aoyiProdIndexList) {
                        if (!exsitItemIdList.contains(aoyiProdIndex.getSkuid())) {
                            insertAoyiProdIndexList.add(aoyiProdIndex);
                        }
                    }

                    log.info("同步itemIdList 第{}页 需要插入数据{}条 内容:{}",
                            pageNumber, insertAoyiProdIndexList.size(), JSONUtil.toJsonStringWithoutNull(insertAoyiProdIndexList));


                    // 执行插入
                    if (CollectionUtils.isNotEmpty(insertAoyiProdIndexList)) {
                        productDao.batchInsert(insertAoyiProdIndexList);
                    }

                    totalInsert = totalInsert + insertAoyiProdIndexList.size();
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
     * @param pageNum
     * @throws Exception alter table `star_sku` add column `merchant_code` varchar(16) not null default '141' comment '供应商code';
     *                   <p>
     *                   alter table `star_sku` add column `merchant_code` varchar(16) not null default '141' comment '供应商code';
     */
    @Override
    public void syncItemDetail(Integer pageNum, Integer maxPageCount) throws Exception {
        try {
            // 1.分页查询itemId列表数据
            ProductQueryBean productQueryBean = new ProductQueryBean();
            productQueryBean.setPageSize(10);
            productQueryBean.setMerchantId(2);
            productQueryBean.setSkuProfix("3");

            //
            Integer totalItemIdCount = 0; // 累计处理的itemId数量
            Integer totalSkuCount = 0; // 累计处理的sku数量
            Integer totalPropertyCount = 0; // 累计处理的商品属性数量

            //
            while (true) {
                // 数据库查询一批spu
                productQueryBean.setPageNo(pageNum);
                PageInfo<AoyiProdIndex> pageInfo = productDao.selectPageable(productQueryBean);

                List<AoyiProdIndex> aoyiProdIndexList = pageInfo.getList();

                log.info("同步商品详情 获取itermIdList 第{}页{}条 >>>>> 数据:{}",
                        pageNum, aoyiProdIndexList.size(), JSONUtil.toJsonStringWithoutNull(aoyiProdIndexList));

                if (CollectionUtils.isEmpty(aoyiProdIndexList)) {
                    log.info("同步商品详情 获取itermId列表 第{}页 无数据 任务停止!");
                    break;
                }

                // 2. 遍历itemId列表，查询详情信息
                int itemIdIndex = 0; // 该for需要的itemId的计数
                for (AoyiProdIndex aoyiProdIndex : aoyiProdIndexList) { // 遍历itemId列表
                    String itemId = aoyiProdIndex.getSkuid();
                    totalItemIdCount++;
                    itemIdIndex++;

                    // 3. rpc 获取商品详情
                    AoyiItemDetailResDto aoyiItemDetailResDto = aoyiClientRpcService.weipinhuiQueryItemDetial(itemId);
                    log.info("同步商品详情 第{}页 第{}个itemId:{} rpc获取商品详情: {}",
                            pageNum, itemIdIndex, itemId, JSONUtil.toJsonString(aoyiItemDetailResDto));

                    if (aoyiItemDetailResDto == null) {
                        log.warn("同步商品详情 第{}页 第{}个itemId:{} rpc获取商品详情为空 继续...", pageNum, itemIdIndex, itemId);
                        continue;
                    }

                    // 4. 入库处理 分为两步 4.1 更新 spu， 4.2 插入 sku
                    // 4.1 更新spu
                    // 获取brand名称
                    AoyiBaseBrand aoyiBaseBrand =
                            aoyiBaseBrandDao.selectByBrandId(Integer.valueOf(aoyiItemDetailResDto.getBrandId()));

                    aoyiProdIndex.setName(aoyiItemDetailResDto.getItemTitle()); // 商品名称spu名称
                    // aoyiProdIndex.setState(aoyiItemDetailResDto.getCanSell() == "true" ? "1" : "0"); // 是否出售 "false" - 是否上架 0：下架；1：上架
                    aoyiProdIndex.setBrand(aoyiBaseBrand == null ? "" : aoyiBaseBrand.getBrandName());
                    aoyiProdIndex.setBrandId(StringUtils.isNotBlank(aoyiItemDetailResDto.getBrandId())
                            ? Integer.valueOf(aoyiItemDetailResDto.getBrandId()) : 0); // 品牌 id
                    aoyiProdIndex.setCategory(aoyiItemDetailResDto.getCategoryId()); // 类目三级 id "50023439"

                    // 处理spu主图
                    List<String> imageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemImage(), List.class);
                    List<AyFcImages> mainSpuImageList =
                            handleAyFcImages(imageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, IMAGE_TYPE_ZT);

                    String imagesUrl =
                            String.join(":", mainSpuImageList.stream().map(m -> m.getFcImage()).collect(Collectors.toList()));
                    aoyiProdIndex.setImagesUrl(imagesUrl); // 商品主图 [http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/1.jpg,http://pic.aoyi365.com/aoyi_tmall/50 000550/ZT/2.jpg,http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/3.jpg,http://pic.aoyi365.co m/aoyi_tmall/50000550/ZT/4.jpg,]", // 商品主图

                    // 处理spu详情图
                    List<String> detailImageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemDetailImage(), List.class);
                    List<AyFcImages> detailSpuImageList =
                            handleAyFcImages(detailImageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, IMAGE_TYPE_XQ);
                    String introductionUrl =
                            String.join(":", detailSpuImageList.stream().map(d -> d.getFcImage()).collect(Collectors.toList()));
                    aoyiProdIndex.setIntroductionUrl(introductionUrl); // 商品详情图 [http://pic.aoyi365

                    log.info("同步商品详情 第{}页 第{}个itemId:{} 更新spu 数据库入参:{}",
                            pageNum, itemIdIndex, itemId, JSONUtil.toJsonStringWithoutNull(aoyiProdIndex));
                    // x..执行更新spu
                    AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(aoyiProdIndex);
                    productDao.updateByPrimaryKeySelective(aoyiProdIndexWithBLOBs);

                    // 4.2 新增sku
                    List<AoyiSkuResDto> aoyiSkuResDtoList = aoyiItemDetailResDto.getAoyiSkusResponses(); // rpc获取到的sku集合
                    if (CollectionUtils.isEmpty(aoyiSkuResDtoList)) {
                        log.warn("同步商品详情 第{}页 第{}个itemId:{} 其商品详情为空 继续......", pageNum, itemIdIndex, itemId);
                        continue;
                    }

                    // 4.2.1 首先判断是否已存在sku
                    // skuId集合
                    List<String> skuIdList =
                            aoyiSkuResDtoList.stream().map(a -> a.getSkuId()).collect(Collectors.toList());

                    // 数据库查询
                    List<StarSku> exsitStarSkuList = starSkuDao.selectBySkuIdList(skuIdList);
                    List<String> exsitSkuIdList =
                            exsitStarSkuList.stream().map(e -> e.getSkuId()).collect(Collectors.toList());

                    // 4.2.2 过滤掉已经存在sku
                    List<StarSku> insertStarSkuList = new ArrayList<>(); // 过滤掉已存在的sku之后，剩下的需要插入的sku信息
                    List<StarProperty> candidateStarPropertyList = new ArrayList<>(); // 待插入的商品规格列表信息
                    List<AyFcImages> skuImageList = new ArrayList<>(); // 待插入ay_fc_images表的信息
                    // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
                    for (AoyiSkuResDto aoyiSkuResDto : aoyiSkuResDtoList) { // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
                        if (!exsitSkuIdList.contains(aoyiSkuResDto.getSkuId())) { // 如果不存在
                            // a.组装sku
                            StarSku starSku = new StarSku();
                            // String code;
                            // purchaseQty;

                            // starSku.setGoodsLogo(aoyiSkuResDto.getSkuImageUrl());
                            starSku.setSkuId(aoyiSkuResDto.getSkuId());
                            starSku.setStatus("false".equals(aoyiSkuResDto.getCanSell()) ? 0 : 1);
                            starSku.setSpuId(itemId);
                            starSku.setAdvisePrice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getSellPrice())); // 建议销售价格 单位分
                            starSku.setSprice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getPriceCent())); // 采购价格 单位分
                            // starSku.setPrice(); // 实际销售价格 单位分
                            starSku.setMerchantCode(MERCHANT_CODE);

                            insertStarSkuList.add(starSku); //

                            // b.组装该sku的商品规格
                            List<StarProperty> _starPropertyList = assembleProdSepc(aoyiSkuResDto.getSkuId(),
                                    aoyiSkuResDto.getSepca(), aoyiSkuResDto.getSepcb(), aoyiSkuResDto.getSepcc());

                            if (CollectionUtils.isNotEmpty(_starPropertyList)) {
                                candidateStarPropertyList.addAll(_starPropertyList);
                            }

                            // c. 组装该sku的图片信息
                            List<AyFcImages> _ayFcImagesList = handleAyFcImages(Arrays.asList(aoyiSkuResDto.getSkuImageUrl()),
                                    aoyiItemDetailResDto.getCategoryId(), itemId, IMAGE_TYPE_XQ);
                            skuImageList.addAll(_ayFcImagesList);

                            //
                            starSku.setGoodsLogo(productConfig.getFengchaoImagePrefix() + _ayFcImagesList.get(0).getFcImage());
                        }
                    }

                    // 4.3 批量插入 sku 和 sku的商品规格
                    // 4.3.1 批量插入 sku
                    log.info("同步商品详情 第{}页 第{}个itemId:{}, 插入sku列表:{}个 数据:{}",
                            pageNum, itemIdIndex, itemId, insertStarSkuList.size(), JSONUtil.toJsonStringWithoutNull(insertStarSkuList));
                    if (CollectionUtils.isNotEmpty(insertStarSkuList)) {
                        starSkuDao.batchInsert(insertStarSkuList);
                        totalSkuCount = totalSkuCount + insertStarSkuList.size();
                    }

                    // 4.3.2 批量插入 sku的商品规格
                    log.info("同步商品详情 第{}页 第{}个itemId:{}, 待处理的proerty列表:{}个 数据:{}",
                            pageNum, itemIdIndex, itemId, candidateStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(candidateStarPropertyList));

                    if (CollectionUtils.isNotEmpty(candidateStarPropertyList)) {
                        // 首先查询数据库是否已经存在
                        List<Integer> candidateProductionIdList = // 待插入的productionid集合
                                candidateStarPropertyList.stream().map(s -> s.getProductId()).collect(Collectors.toList());

                        // 已存在的数据
                        List<StarProperty> exsitStarPropertyList = starPropertyDao.selectByProductIds(candidateProductionIdList);
                        List<Integer> exsitPropertyIdList = exsitStarPropertyList.stream().map(e -> e.getProductId()).collect(Collectors.toList());

                        // 需要出入的数据
                        List<StarProperty> insertStarPropertyList = new ArrayList<>();

                        // 过滤
                        for (StarProperty candidateStarProperty : candidateStarPropertyList) {
                            if (!exsitPropertyIdList.contains(candidateStarProperty.getProductId())) {
                                insertStarPropertyList.add(candidateStarProperty);
                            }
                        }

                        log.info("同步商品详情 第{}页 第{}个itemId:{}, 插入的proerty列表:{}个 数据:{}",
                                pageNum, itemIdIndex, itemId, insertStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(insertStarPropertyList));
                        //
                        if (CollectionUtils.isNotEmpty(insertStarPropertyList)) {
                            starPropertyDao.batchInsert(insertStarPropertyList);
                        }
                        totalPropertyCount = totalPropertyCount + insertStarPropertyList.size();
                    }

                    // 5. 处理图片信息: mainSpuImageList detailSpuImageList skuImageList
                    mainSpuImageList.addAll(detailSpuImageList);
                    mainSpuImageList.addAll(skuImageList);

                    log.info("同步商品详情 第{}页 第{}个itemId:{} 插入图片数据:{}条 数据:{}",
                            pageNum, itemIdIndex, itemId, mainSpuImageList.size(), JSONUtil.toJsonStringWithoutNull(mainSpuImageList));
                    if (CollectionUtils.isNotEmpty(mainSpuImageList)) {
                        ayFcImagesDao.batchInsert(mainSpuImageList);
                    }
                }// end for

                // 6. 停止条件
                if (maxPageCount != -1 && pageNum >= maxPageCount) {
                    log.info("同步商品详情 第{}页 达到最大查询页数限制:{} 停止!", pageNum, maxPageCount);

                    break;
                }

                pageNum++;
            } // end while

            log.info("同步商品详情 结束<<<<<< 共处理 itemId:{}个, 插入sku:{}个, property:{}个",
                    totalItemIdCount, totalSkuCount, totalPropertyCount);
        } catch (Exception e) {
            log.error("同步商品 异常:{}", e.getMessage(), e);
        }
    }

    ///======================================== private ===================================================

    /**
     * 组装sku的商品规格
     *
     * @param skuId
     * @param sepca
     * @param sepcb
     * @param sepcc
     * @return
     */
    private List<StarProperty> assembleProdSepc(String skuId, String sepca, String sepcb, String sepcc) {
        List<StarProperty> starPropertyList = new ArrayList<>();

        if (StringUtils.isNotBlank(sepca)) {
            StarProperty starProperty = new StarProperty();
            starProperty.setType(0);
            starProperty.setName("sepca");
            starProperty.setProductId(Integer.valueOf(skuId));
            starProperty.setVal(sepca);

            starPropertyList.add(starProperty);
        }

        if (StringUtils.isNotBlank(sepcb)) {
            StarProperty starProperty = new StarProperty();
            starProperty.setType(0);
            starProperty.setName("sepcb");
            starProperty.setProductId(Integer.valueOf(skuId));
            starProperty.setVal(sepcb);

            starPropertyList.add(starProperty);
        }

        if (StringUtils.isNotBlank(sepcc)) {
            StarProperty starProperty = new StarProperty();
            starProperty.setType(0);
            starProperty.setName("sepcc");
            starProperty.setProductId(Integer.valueOf(skuId));
            starProperty.setVal(sepcc);

            starPropertyList.add(starProperty);
        }

        return starPropertyList;
    }

    /**
     * 处理图片地址
     *
     * @param urlList ["aoyi_vip/aoyi30010k/30008601/ZT/30014191/1.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/2.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/3.jpg"aoyi_vip/aoyi30010k/30008601/ZT/30014191/4.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/6.jpg"]
     * @return
     */
    private List<AyFcImages> handleAyFcImages(List<String> urlList, String categoryId, String spuId, String type) {
        List<AyFcImages> ayFcImagesList = new ArrayList<>();

        for (String imageUrl : urlList) {
            String path = "/" + categoryId + "/" + spuId + "/";
            String name = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());

            AyFcImages ayFcImages = new AyFcImages();
            ayFcImages.setFcImage(path + type + "/" + name); //
            ayFcImages.setAyImage(productConfig.getWeipinhuiImagePrefix() + imageUrl);
            ayFcImages.setPath(path);
            ayFcImages.setType(type);
            ayFcImages.setCreatedAt(new Date());
            ayFcImages.setUpdatedAt(new Date());

            ayFcImagesList.add(ayFcImages);
        }

        return ayFcImagesList;
    }

    /**
     *
     * @param aoyiProdIndex
     * @return
     */
    private AoyiProdIndexWithBLOBs convertToAoyiProdIndexWithBLOBs(AoyiProdIndex aoyiProdIndex) {
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();

        aoyiProdIndexWithBLOBs.setId(aoyiProdIndex.getId());
        aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndex.getSkuid());
        aoyiProdIndexWithBLOBs.setBrand(aoyiProdIndex.getBrand());
        aoyiProdIndexWithBLOBs.setCategory(aoyiProdIndex.getCategory());
        aoyiProdIndexWithBLOBs.setImage(aoyiProdIndex.getImage());
        aoyiProdIndexWithBLOBs.setModel(aoyiProdIndex.getModel());
        aoyiProdIndexWithBLOBs.setName(aoyiProdIndex.getName());
        aoyiProdIndexWithBLOBs.setWeight(aoyiProdIndex.getWeight());
        aoyiProdIndexWithBLOBs.setUpc(aoyiProdIndex.getUpc());
        aoyiProdIndexWithBLOBs.setSaleunit(aoyiProdIndex.getSaleunit());
        aoyiProdIndexWithBLOBs.setState(aoyiProdIndex.getState());
        aoyiProdIndexWithBLOBs.setPrice(aoyiProdIndex.getPrice());
        aoyiProdIndexWithBLOBs.setIprice(aoyiProdIndex.getIprice());
        aoyiProdIndexWithBLOBs.setSprice(aoyiProdIndex.getSprice());
        aoyiProdIndexWithBLOBs.setImagesUrl(aoyiProdIndex.getImagesUrl());
        aoyiProdIndexWithBLOBs.setIntroductionUrl(aoyiProdIndex.getIntroductionUrl());
        aoyiProdIndexWithBLOBs.setMerchantId(aoyiProdIndex.getMerchantId());
        aoyiProdIndexWithBLOBs.setInventory(aoyiProdIndex.getInventory());
        aoyiProdIndexWithBLOBs.setBrandId(aoyiProdIndex.getBrandId());
        aoyiProdIndexWithBLOBs.setMpu(aoyiProdIndex.getMpu());
        aoyiProdIndexWithBLOBs.setType(aoyiProdIndex.getType());
        aoyiProdIndexWithBLOBs.setCompareUrl(aoyiProdIndex.getCompareUrl());
        aoyiProdIndexWithBLOBs.setSubTitle(aoyiProdIndex.getSubTitle());
        aoyiProdIndexWithBLOBs.setComparePrice(aoyiProdIndex.getComparePrice());
        aoyiProdIndexWithBLOBs.setTaxRate(aoyiProdIndex.getTaxRate());
        aoyiProdIndexWithBLOBs.setFloorPrice(aoyiProdIndex.getFloorPrice());
        aoyiProdIndexWithBLOBs.setCrossBorder(aoyiProdIndex.getCrossBorder());

        return aoyiProdIndexWithBLOBs;
    }

}
