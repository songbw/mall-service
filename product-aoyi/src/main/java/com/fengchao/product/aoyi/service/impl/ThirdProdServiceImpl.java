package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.config.ProductConfig;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.mapper.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.service.ThirdProdService;
import com.fengchao.product.aoyi.utils.AsyncTask;
import com.fengchao.product.aoyi.utils.HttpClient;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ThirdProdServiceImpl implements ThirdProdService {

    private static Logger logger = LoggerFactory.getLogger(ThirdProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper prodMapper;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private AyFcImagesDao ayFcImagesDao;
    @Autowired
    private BaseService baseService;
    @Autowired
    private PlatformDao platformDao ;
    @Autowired
    private AsyncTask asyncTask ;
    @Autowired
    private CategoryDao categoryDao ;
    @Autowired
    private AoyiBaseBrandXMapper baseBrandXMapper;
    @Autowired
    private AoyiClientService aoyiClientService ;
    @Autowired
    private AoyiProdIndexMapper aoyiProdIndexMapper ;
    @Autowired
    private  StarDetailImgMapper starDetailImgMapper;
    @Autowired
    private StarPropertyMapper starPropertyMapper;
    @Autowired
    private StarSkuMapper starSkuMapper ;
    @Autowired
    private StarSkuDao starSkuDao ;
    @Autowired
    private StarCategoryMapper starCategoryMapper ;
    @Autowired
    private AoyiClientRpcService aoyiClientRpcService ;
    private AoyiBaseBrandDao aoyiBaseBrandDao;
    private ProductConfig productConfig;
    private StarPropertyDao starPropertyDao ;

    @Override
    public OperaResult add(AoyiProdIndexX bean){
        OperaResult result = new OperaResult();
        // sku不能为空，不能重复
        if (StringUtils.isEmpty(bean.getSkuid())){
            result.setCode(2000001);
            result.setMsg("skuid 不能为空。");
            return result;
        }
        // brand不能为空
        if (StringUtils.isEmpty(bean.getBrand())){
            result.setCode(2000002);
            result.setMsg("brand 不能为空。");
            return result;
        }
        // category不能为空
        if (StringUtils.isEmpty(bean.getCategory())){
            result.setCode(2000003);
            result.setMsg("category 不能为空。");
            return result;
        }
        // name不能为空
        if (StringUtils.isEmpty(bean.getName())){
            result.setCode(2000005);
            result.setMsg("name 不能为空。");
            return result;
        }
        // price不能为空
        if (StringUtils.isEmpty(bean.getPrice())){
            result.setCode(2000006);
            result.setMsg("price 不能为空。");
            return result;
        }
        // sprice不能为空
        if (StringUtils.isEmpty(bean.getSprice())){
            result.setCode(2000007);
            result.setMsg("sprice 不能为空。");
            return result;
        }
        // state不能为空
        if (StringUtils.isEmpty(bean.getState())){
            result.setCode(2000008);
            result.setMsg("state 不能为空。");
            return result;
        }
        // 主图不能为空
        if (bean.getZtImage() == null || bean.getZtImage().size() <= 0){
            result.setCode(2000009);
            result.setMsg("ztImage 不能为空。");
            return result;
        }
        // 详情图不能为空
        if (bean.getXqImage() == null || bean.getXqImage().size() <= 0){
            result.setCode(2000010);
            result.setMsg("xqImage 不能为空。");
            return result;
        }
        List<AoyiProdIndex> aoyiProdIndices =  productDao.findBySkuId(bean.getSkuid(), 2);
        if (aoyiProdIndices.size() > 0) {
            result.setCode(2000011);
            result.setMsg("skuid 重复。");
            return result;
        }
        Date date = new Date();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setMpu(bean.getSkuid());
        bean.setMerchantId(2);
        bean.setMerchantCode(bean.getSkuid().substring(bean.getSkuid().length() -2,bean.getSkuid().length())) ;
        packImg(bean);
        // 插入商品信息
        productDao.insertX(bean);
        return result;
    }

    @Override
    public OperaResult update(AoyiProdIndexX bean) {
        OperaResult result = new OperaResult();
        // sku不能为空
        if (StringUtils.isEmpty(bean.getSkuid())){
            result.setCode(2000001);
            result.setMsg("skuid 不能为空。");
            return result;
        }
        List<AoyiProdIndex> aoyiProdIndices = productDao.findBySkuId(bean.getSkuid(), 2) ;
        if (aoyiProdIndices == null || aoyiProdIndices.size() == 0) {
            result.setCode(2000001);
            result.setMsg("skuid "+ bean.getSkuid() +" 不存在，请重新添加。");
            return result;
        }
        packImg(bean);
        // 修改商品信息
        bean.setMerchantId(2);
        productDao.update(bean);
        return result;
    }

    @Override
    public OperaResult insertOrUpdateByMpu(AoyiProdIndex bean) {
        OperaResult result = new OperaResult();
        // mpu不能为空
        if (StringUtils.isEmpty(bean.getMpu())){
            result.setCode(2000001);
            result.setMsg("mpu 不能为空。");
            return result;
        }
        List<AoyiProdIndex> aoyiProdIndices = productDao.findByMpu(bean.getMpu(), bean.getMerchantId()) ;
        if (aoyiProdIndices == null || aoyiProdIndices.size() == 0) {
            // 添加商品信息
            productDao.insertByMpu(bean) ;
        } else {
            // 修改商品信息
            productDao.updateByMpu(bean);
        }
        return result;
    }

    private void packImg(AoyiProdIndexX bean) {
        String path = "/"+ bean.getCategory() + "/"+ bean.getSkuid() + "/";
        // 组装主图字段，添加图片
        if (bean.getZtImage() != null && bean.getZtImage().size() > 0){
            bean.getZtImage().forEach(zt -> {
                String ztarray[] = zt.split("ZT");
                if (StringUtils.isEmpty(bean.getImagesUrl())){
                    bean.setImagesUrl(path + "ZT" + ztarray[1]);
                } else {
                    bean.setImagesUrl(bean.getImagesUrl() + ":"+ path + "ZT" + ztarray[1]);
                }
                AyFcImages ayFcImages = new AyFcImages();
                ayFcImages.setAyImage(zt);
                ayFcImages.setFcImage(path + "ZT" + ztarray[1]);
                ayFcImages.setStatus(0);
                Date  date1 = new Date();
                ayFcImages.setCreatedAt(date1);
                ayFcImages.setUpdatedAt(date1);
                ayFcImages.setPath(path);
                ayFcImages.setType("ZT");
                ayFcImagesDao.insert(ayFcImages) ;
            });
        }
        // 组装详情图字段，添加图片对应表
        if (bean.getXqImage() != null && bean.getXqImage().size() > 0){
            bean.getXqImage().forEach(xq -> {
                String xqarray[] = xq.split("XQ");
                if (StringUtils.isEmpty(bean.getIntroductionUrl())){
                    bean.setIntroductionUrl(path + "XQ" + xqarray[1]);
                } else {
                    bean.setIntroductionUrl(bean.getIntroductionUrl() + ":"+ path + "XQ" + xqarray[1]);
                }
                AyFcImages ayFcImages = new AyFcImages();
                ayFcImages.setAyImage(xq);
                ayFcImages.setFcImage(path + "XQ" + xqarray[1]);
                ayFcImages.setStatus(0);
                Date  date1 = new Date();
                ayFcImages.setCreatedAt(date1);
                ayFcImages.setUpdatedAt(date1);
                ayFcImages.setPath(path);
                ayFcImages.setType("XQ");
                ayFcImagesDao.insert(ayFcImages) ;
            });
        }
    }

    @Override
    public void updatePrice(PriceBean bean) {
        productDao.updatePrice(bean);
        if (!StringUtils.isEmpty(bean.getSpuId())) {
            // 更新价格
            starSkuDao.updatePriceByCodeAndSpuId(bean) ;
        }
    }

    @Override
    public OperaResponse updateState(StateBean bean) {
        OperaResponse operaResponse = new OperaResponse();
        if ("1".equals(bean.getState())) {
            AoyiProdIndex aoyiProdIndex = new AoyiProdIndex() ;
            if (StringUtils.isEmpty(bean.getSpuId())) {
                aoyiProdIndex = productDao.selectByMpu(bean.getSkuId()) ;
            } else {
                aoyiProdIndex = productDao.selectByMpu(bean.getSpuId()) ;
            }
            logger.debug("第三方更新状态接口，商品信息：{}", JSONUtil.toJsonString(aoyiProdIndex));
            if (org.apache.commons.lang.StringUtils.isEmpty(aoyiProdIndex.getCategory())) {
                operaResponse.setCode(200100);
                operaResponse.setMsg("类别不能为空");
                return operaResponse ;
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(aoyiProdIndex.getPrice())) {
                operaResponse.setCode(200101);
                operaResponse.setMsg("销售价格不能为空");
                return operaResponse ;
            }
//            if (org.apache.commons.lang.StringUtils.isEmpty(aoyiProdIndex.getImage())) {
//                operaResponse.setCode(200102);
//                operaResponse.setMsg("封面图不能为空");
//                return operaResponse ;
//            }
            if (org.apache.commons.lang.StringUtils.isEmpty(aoyiProdIndex.getImagesUrl())) {
                operaResponse.setCode(200103);
                operaResponse.setMsg("主图不能为空");
                return operaResponse ;
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(aoyiProdIndex.getIntroductionUrl())) {
                operaResponse.setCode(200104);
                operaResponse.setMsg("详情图不能为空");
                return operaResponse ;
            }
        }

        if (!StringUtils.isEmpty(bean.getSpuId())) {
            // 更新状态
            starSkuDao.updateStatusByCodeAndSpuId(bean) ;
            bean.setSkuId(bean.getSpuId());
            // 上架状态数量
            if ("0".equals(bean.getState())) {
                List<StarSku> starSkus = starSkuDao.selectUBySpuId(bean.getSpuId());
                if ((starSkus == null || starSkus.size()==0)) {
                    productDao.updateState(bean);
                }
            } else {
                productDao.updateState(bean);
            }
        } else {
            productDao.updateState(bean);
        }

        return operaResponse ;
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            prodMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
    }

    @Override
    public void uploadProdImage() {
        List<AyFcImages> ayFcImages = ayFcImagesDao.findNoUploadImage();
        logger.debug("下载图片任务 数量为:{}", ayFcImages.size());
        if (ayFcImages != null && ayFcImages.size() > 0) {
            ayFcImages.forEach(image -> {
                OperaResult result = baseService.downUpload(image);

                logger.debug("下载图片任务: {} 返回结果: {}",
                        JSONUtil.toJsonString(image), JSONUtil.toJsonString(result));
//                if (result.getCode() == 200) {
//                    image.setStatus(1);
//                    ayFcImagesDao.updateStatus(image);
//                } else {
//                    logger.debug("调用base服务失败：{}", JSONUtil.toJsonString(result));
//                }
            });
        }
    }

    @Override
    public OperaResponse sync(ThirdSyncBean bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getPlatformId())) {
            response.setCode(2000004);
            response.setMsg("platformId 不能为null");
            return response ;
        }
        List<AoyiProdIndex> prodIndices = new ArrayList<>() ;
        if (bean.getBrands() != null && bean.getBrands().size() > 0) {
            prodIndices.addAll(productDao.selectByBrand(bean.getBrands())) ;
        }
        if (bean.getCategories() != null && bean.getCategories().size() > 0) {
            prodIndices.addAll(productDao.selectByCategory(bean.getCategories())) ;
        }
        if (bean.getMerchants() != null && bean.getMerchants().size() > 0) {
            prodIndices.addAll(productDao.selectByMerchant(bean.getMerchants())) ;
        }
        if (bean.getMpus() != null && bean.getMpus().size() > 0) {
            prodIndices.addAll(productDao.selectByMpu(bean.getMpus())) ;
        }
        Platform platform = platformDao.selectByAppId(bean.getPlatformId()) ;
        if (platform == null) {
            response.setCode(2000005);
            response.setMsg("platformId 不存在");
            return response ;
        }
        WebTarget webTarget = HttpClient.createClient().target(platform.getGatewayUrl() + "third/prod/receive");
        asyncTask.executeAsyncProductTask(productDao, webTarget, prodIndices) ;
        return response;
    }

    @Override
    public OperaResponse syncCategory(CategorySyncBean bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getPlatformId())) {
            response.setCode(2000004);
            response.setMsg("platformId 不能为null");
            return response ;
        }
        Platform platform = platformDao.selectByAppId(bean.getPlatformId()) ;
        if (platform == null) {
            response.setCode(2000005);
            response.setMsg("platformId 不存在");
            return response ;
        }
        List<AoyiBaseCategory> categories = new ArrayList<>() ;
        if (bean.getCategories() != null && bean.getCategories().size() > 0) {
            categories = categoryDao.selectByCategoryIds(bean.getCategories()) ;
        }
        WebTarget webTarget = HttpClient.createClient().target(platform.getGatewayUrl() + "third/prod/category/receive");
        asyncTask.executeAsyncCategoryTask(webTarget,categories);
        return response;
    }

    @Override
    public OperaResponse syncBrand(ThirdSyncBean bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getPlatformId())) {
            response.setCode(2000004);
            response.setMsg("platformId 不能为null");
            return response ;
        }
        Platform platform = platformDao.selectByAppId(bean.getPlatformId()) ;
        if (platform == null) {
            response.setCode(2000005);
            response.setMsg("platformId 不存在");
            return response ;
        }
        List<AoyiBaseBrandX> baseBrands = new ArrayList<>() ;
        if (bean.getBrands() != null && bean.getBrands().size() > 0) {
            baseBrands = baseBrandXMapper.selectByBrandIdList(bean.getBrands()) ;
        }
        WebTarget webTarget = HttpClient.createClient().target(platform.getGatewayUrl() + "third/prod/brand/receive");
        asyncTask.executeAsyncBrandTask(webTarget,baseBrands);
        return response;
    }

    @Override
    public OperaResponse updateAyFcImageStatus(Long id, Integer status) {
        OperaResponse response = new OperaResponse() ;
        AyFcImages images = new AyFcImages() ;
        images.setId(id);
        images.setStatus(status);
        images.setUpdatedAt(new Date());
        ayFcImagesDao.updateStatus(images);
        return response;
    }

    @Override
    public OperaResponse syncStarProd() {
        logger.debug("syncStarProd");
        OperaResponse response = new OperaResponse() ;
        asyncTask.executeAsyncStarProd(aoyiClientService, productDao, aoyiProdIndexMapper, starDetailImgMapper, starPropertyMapper, starSkuMapper, starSkuDao);
        return response;
    }

    @Override
    public OperaResponse syncStarProdPrice() {
        logger.debug("syncStarProdPrice");
        OperaResponse response = new OperaResponse() ;
        asyncTask.executeAsyncStarProdPrice(aoyiClientService, starSkuDao, productDao);
        return response;
    }

    @Override
    public OperaResponse syncStarCategory() {
        OperaResponse response = new OperaResponse() ;
        asyncTask.executeAsyncStarCategory(aoyiClientService, starCategoryMapper);
        return response;
    }

    @Override
    public OperaResponse asyncWphItemDetail(Integer pageNumber, Integer maxPageCount) {

        try {
            int pageCount = 0;
            int totalInsert = 0; // 记录一下执行一共插入的数据数量
            //接收集合各段的 执行的返回结果
            List <Future<String>> futureList = new ArrayList <Future<String>>();
            while (true) {
                // 1. 获取数据
                List<AoyiItemDetailResDto> aoyiItemDetailResDtoList
                        = aoyiClientRpcService.weipinhuiQueryItemsList(pageNumber, 20);

                logger.debug("同步itemIdList 第{}页 共查询到{}条数据 >>>> {}",
                        pageNumber, aoyiItemDetailResDtoList.size(), JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDtoList));


                // 2. 入库处理
                if (CollectionUtils.isNotEmpty(aoyiItemDetailResDtoList)) {
                    List<String> itemIdList = new ArrayList<>();
                    for (AoyiItemDetailResDto aoyiItemDetailResDto : aoyiItemDetailResDtoList) {

                        itemIdList.add(aoyiItemDetailResDto.getItemId());
                    }

                    // 查询数据库是否已存在数据
                    List<AoyiProdIndexWithBLOBs> exsitAoyiprodIndexList =
                            productDao.selectAoyiProdIndexListByMpuIdList(itemIdList);

                    List<String> exsitItemIdList =
                            exsitAoyiprodIndexList.stream().map(e -> e.getSkuid()).collect(Collectors.toList());

                    // 去掉已存在的数据
                    List<String> newItemList = new ArrayList<>(); // 准备插入的数据
                    for (String item : itemIdList) {
                        if (!exsitItemIdList.contains(item)) {
                            newItemList.add(item);
                        }
                    }
                    if (newItemList != null && newItemList.size() > 0) {
                        futureList.add(asyncTask.executeAsyncWphItemDetailForSub(newItemList, aoyiClientRpcService, aoyiBaseBrandDao, productConfig, productDao, starSkuDao, starPropertyDao, ayFcImagesDao,aoyiProdIndexMapper));
                    }

                }

                // 3. 判断是否需要继续同步
                if (aoyiItemDetailResDtoList.size() == 0) {
                    logger.debug("同步itemIdList 结束");
                    break;
                }

                logger.debug("同步itemIdList 第{}页 累计插入数据{}条", pageNumber, totalInsert);

                pageNumber++;
                pageCount++;

                //
                if (maxPageCount != -1 && pageCount >= maxPageCount) {
                    logger.warn("同步itemIdList 达到最大页数{}限制 停止同步!", maxPageCount);
                    break;
                }
            } // end while

            //对各个线程段结果进行解析
            for (Future<String> future : futureList) {
                String str;
                if (null != future) {
                    try {
                        str = future.get().toString();
                        logger.debug("current thread id =" + Thread.currentThread().getName() + ",result=" + str);
                    } catch (InterruptedException | ExecutionException e) {
                        logger.debug("线程运行异常！");
                    }
                } else {
                    logger.debug("线程运行异常！");
                }
            }
        } catch (Exception e) {
            logger.error("同步品牌 异常:{}", e.getMessage(), e);
        }

//        asyncTask.executeAsyncWphItemDetail(aoyiClientRpcService, aoyiBaseBrandDao, productConfig, productDao, starSkuDao, starPropertyDao, ayFcImagesDao);
        return new OperaResponse();
    }

}
