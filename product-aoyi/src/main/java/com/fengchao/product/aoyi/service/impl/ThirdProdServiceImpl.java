package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.dao.AyFcImagesDao;
import com.fengchao.product.aoyi.dao.PlatformDao;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.AyFcImages;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.service.ThirdProdService;
import com.fengchao.product.aoyi.utils.AsyncTask;
import com.fengchao.product.aoyi.utils.HttpClient;
import com.fengchao.product.aoyi.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    }

    @Override
    public void updateState(StateBean bean) {
        productDao.updateState(bean);
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
        if (ayFcImages != null && ayFcImages.size() > 0) {
            ayFcImages.forEach(image -> {
                OperaResult result = baseService.downUpload(image);
                if (result.getCode() == 200) {
                    image.setStatus(1);
                    ayFcImagesDao.updateStatus(image);
                } else {
                    logger.info("调用base服务失败：{}", JSONUtil.toJsonString(result));
                }
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
        asyncTask.executeAsyncTask(productDao, webTarget, prodIndices) ;
        return response;
    }

}
