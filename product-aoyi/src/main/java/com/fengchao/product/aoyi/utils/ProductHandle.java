package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.StarSkuBean;
import com.fengchao.product.aoyi.config.ESConfig;
import com.fengchao.product.aoyi.config.MerchantCodeBean;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@EnableConfigurationProperties({ESConfig.class})
@Service
@Slf4j
public class ProductHandle {

    private StarSkuDao starSkuDao ;
    private AppSkuPriceDao appSkuPriceDao ;
    private AppSkuStateDao appSkuStateDao ;
    private StarDetailImgDao starDetailImgDao ;
    private StarPropertyDao starPropertyDao ;
    private VendorsRpcService vendorsRpcService;
    private ESConfig config;
    private AoyiBaseCategoryXMapper categoryXMapper ;

    @Autowired
    public ProductHandle(StarSkuDao starSkuDao, AppSkuPriceDao appSkuPriceDao, AppSkuStateDao appSkuStateDao, StarDetailImgDao starDetailImgDao,StarPropertyDao starPropertyDao, VendorsRpcService vendorsRpcService, ESConfig config,AoyiBaseCategoryXMapper categoryXMapper) {
        this.starSkuDao = starSkuDao;
        this.appSkuPriceDao = appSkuPriceDao;
        this.appSkuStateDao = appSkuStateDao;
        this.starDetailImgDao = starDetailImgDao;
        this.starPropertyDao = starPropertyDao ;
        this.vendorsRpcService = vendorsRpcService;
        this.config = config ;
        this.categoryXMapper = categoryXMapper ;
    }

    public AoyiProdIndexX updateImage(AoyiProdIndexX aoyiProdIndexX) {
        if (aoyiProdIndexX.getImage() == null || "".equals(aoyiProdIndexX.getImage())) {
            String imageUrl = aoyiProdIndexX.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image;
                if (imageUrl.indexOf("/") == 0) {
                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                } else {
                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                }
                aoyiProdIndexX.setImage(image);
            }
        }
        return aoyiProdIndexX;
    }

    /**
     * 获取多sku的商品主图
     * @param aoyiProdIndexX
     */
    public void getStarSkuImagesUrl(AoyiProdIndexX aoyiProdIndexX) {
        if (StringUtils.isEmpty(aoyiProdIndexX.getImagesUrl())) {
            List<StarDetailImg> starDetailImgs = starDetailImgDao.selectBySpuId(aoyiProdIndexX.getId()) ;
            String imageUrl = "" ;
            if (starDetailImgs != null && starDetailImgs.size() > 0) {
                for (int i = 0; i < starDetailImgs.size(); i++) {
                    if (i == 0) {
                        imageUrl = starDetailImgs.get(i).getImgUrl() ;
                    } else {
                        imageUrl = imageUrl + ";" + starDetailImgs.get(i).getImgUrl() ;
                    }
                }
                aoyiProdIndexX.setImagesUrl(imageUrl);
            }
        }
    }

    /**
     * 批量获取多sku的商品主图
     * @param aoyiProdIndexXList
     */
    public void batchGetStarSkuImagesUrl(List<AoyiProdIndexX> aoyiProdIndexXList) {
        List<Integer> ids = aoyiProdIndexXList.stream().map(prodIndexX -> prodIndexX.getId()).collect(Collectors.toList()) ;
        List<StarDetailImg> starDetailImgs = starDetailImgDao.selectBySpuIds(ids) ;
        aoyiProdIndexXList.forEach(aoyiProdIndexX -> {
            if (StringUtils.isEmpty(aoyiProdIndexX.getImagesUrl())) {
                if (starDetailImgs != null && starDetailImgs.size() > 0) {
                    String imageUrl = "" ;
                    for (int i = 0; i < starDetailImgs.size(); i++) {
                        StarDetailImg starDetailImg = starDetailImgs.get(i);
                        if (aoyiProdIndexX.getId() == starDetailImg.getSpuId()) {
                            if (i == 0) {
                                imageUrl = starDetailImgs.get(i).getImgUrl() ;
                            } else {
                                imageUrl = imageUrl + ";" + starDetailImgs.get(i).getImgUrl() ;
                            }
                            aoyiProdIndexX.setImagesUrl(imageUrl);
                            starDetailImgs.remove(i) ;
                            i = i - 1;
                        }
                    }
                }
            }
        });
    }

    public AoyiProdIndex updateImageExample(AoyiProdIndex aoyiProdIndex) {
        if (aoyiProdIndex.getImage() == null || "".equals(aoyiProdIndex.getImage())) {
            String imageUrl = aoyiProdIndex.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image ;
                if (imageUrl.indexOf("/") == 0) {
                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                } else {
                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                }
                aoyiProdIndex.setImage(image);
            }
        }
        return aoyiProdIndex;
    }

    public AoyiProdIndexXWithBLOBs updateImageWithBLOBS(AoyiProdIndexXWithBLOBs aoyiProdIndexX) {
        if (aoyiProdIndexX.getImage() == null || "".equals(aoyiProdIndexX.getImage())) {
            String imageUrl = aoyiProdIndexX.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image;
                if (imageUrl.indexOf("/") == 0) {
                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                } else {
                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                }
                aoyiProdIndexX.setImage(image);
            }
        }
        return aoyiProdIndexX;
    }

    /**
     * 将数据库中查询的商品信息与关联表进行整合
     * @param prodIndexList  数据库中的商品信息列表
     * @param queryBean   商品查询条件
     * @return    整合后的商品信息数据
     */
    public List<AoyiProdIndexX> convertProdIndexListToXList(List<AoyiProdIndex> prodIndexList, ProductQueryBean queryBean) {
//        List<AoyiProdIndexX> prodIndexXList = new ArrayList<>() ;
//        if (prodIndexList != null) {
//            prodIndexList.forEach(aoyiProdIndex -> {
//                // 获取全部商品信息
//                prodIndexXList.add(convertProductXByProd(aoyiProdIndex, queryBean.getRenterId())) ;
//            });
//        }
//        return prodIndexXList ;
        List<AoyiProdIndexX> prodIndexXList = prodIndexList.stream().map(prodIndex -> {
            AoyiProdIndexX prodIndexX = new AoyiProdIndexX();
            BeanUtils.copyProperties(prodIndex, prodIndexX);
            // 图片全路径
            updateImage(prodIndexX) ;
            return prodIndexX;
        }).collect(Collectors.toList());
        // TODO star sku images url
        batchGetStarSkuImagesUrl(prodIndexXList);
        // batch add star sku
        batchAddStarSkuList(prodIndexXList, queryBean.getRenterId()) ;
        return prodIndexXList;
    }

    /**
     * 获取商品详情
     * @param prodIndex 商品信息
     * @param renterId  租户ID
     * @return 商品详情
     */
    public AoyiProdIndexX convertProductXByProd(AoyiProdIndex prodIndex, String renterId) {
        AoyiProdIndexX prodIndexX = new AoyiProdIndexX() ;
        BeanUtils.copyProperties(prodIndex, prodIndexX);
        // 图片全路径
        updateImage(prodIndexX) ;
        // 获取多sku主图信息
        getStarSkuImagesUrl(prodIndexX);
        // 添加star sku列表
        addStarSkuList(prodIndexX, renterId) ;
        // 添加property
        setPropertyList(prodIndexX);
        return prodIndexX;
    }

    /**
     * 设置ProductX
     * @param prodIndexX 商品详情
     * @param renterId  租户ID
     */
    public void setProductX(AoyiProdIndexX prodIndexX, String renterId) {
        // 图片全路径
        updateImage(prodIndexX) ;
        // 获取多sku主图信息
        getStarSkuImagesUrl(prodIndexX);
        // 添加star sku列表
        addStarSkuList(prodIndexX, renterId) ;
        // 添加property
        setPropertyList(prodIndexX);
    }

    /**
     * 设置ProductX
     * @param prodIndexX 商品详情
     * @param renterId  租户ID
     */
    public void setProductXClient(AoyiProdIndexX prodIndexX, String renterId) {
        // 设置租户状态
        setRenterStateByMpu(renterId, prodIndexX);
        if ("1".equals(prodIndexX.getState())) {
            // 图片全路径
            updateImage(prodIndexX) ;
            // 获取多sku主图信息
            getStarSkuImagesUrl(prodIndexX);
            // 添加star sku列表
            addStarSkuListClient(prodIndexX, renterId) ;

            // 设置租户价格
            setRenterPriceByMpu(renterId, prodIndexX);

            // 添加property
            setPropertyList(prodIndexX);
        }
    }

    /**
     * 批量设置客户端数据
     * @param prodIndexXES
     * @param renterId
     */
    public void batchSetProductXClient(List<AoyiProdIndexX> prodIndexXES, String renterId) {
        // batch get star sku image url
        batchGetStarSkuImagesUrl(prodIndexXES);
        // TODO batch set productxXES


    }

    public void batchGetStarSkuListByMpuForClient(List<AoyiProdIndexX> prodIndexXES, String renterId) {
        List<String> skuIds = prodIndexXES.stream().filter(prodIndexX -> prodIndexX.getType() == 2).map(prodIndexX -> prodIndexX.getSkuid()).collect(Collectors.toList());
        List<String> mpuIds = prodIndexXES.stream().filter(prodIndexX -> prodIndexX.getType() != 2).map(prodIndexX -> prodIndexX.getMpu()).collect(Collectors.toList());
        // 租户价格列表
        List<AppSkuPrice> appSkuPrices = appSkuPriceDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpuIds, mpuIds);
        List<AppSkuState> appSkuStates = appSkuStateDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpuIds, mpuIds);
        // TODO SET PRODS STAR SKU
        List<StarSkuBean> starSkuBeans = batchGetStarSkuListByMpuClient(skuIds, renterId);

        // set star sku bean
        prodIndexXES.forEach(prodIndexX -> {
            if (prodIndexX.getType() == 2) {
                List<StarSkuBean> starSkuBeanList = new ArrayList<>();
                for (int i = 0; i < starSkuBeans.size(); i++) {
                    StarSkuBean starSkuBean = starSkuBeans.get(i);
                    if (prodIndexX.getSkuid().equals(starSkuBean.getSpuId())) {
                        starSkuBeanList.add(starSkuBean) ;
                        starSkuBeans.remove(i) ;
                        i = i - 1 ;
                    }
                }
                if (starSkuBeanList != null && starSkuBeanList.size() > 0) {
                    prodIndexX.setSkuList(starSkuBeanList);
                }
            }
            // set prod sku price
            List<AppSkuPrice> appSkuPriceList = new ArrayList<>();
            for (int i = 0; i < appSkuPrices.size(); i++) {
                AppSkuPrice appSkuPrice = appSkuPrices.get(i);
                if (prodIndexX.getMpu().equals(appSkuPrice.getMpu())) {
                    appSkuPriceList.add(appSkuPrice) ;
                    appSkuPrices.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuPriceList != null && appSkuPriceList.size() > 0) {
                prodIndexX.setPrice(appSkuPriceList.get(0).getPrice().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
            }
            // set prod sku state
            List<AppSkuState> appSkuStateList = new ArrayList<>();
            for (int i = 0; i < appSkuStates.size(); i++) {
                AppSkuState appSkuState = appSkuStates.get(i);
                if (prodIndexX.getMpu().equals(appSkuState.getMpu())) {
                    appSkuStateList.add(appSkuState) ;
                    appSkuStates.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuStateList != null && appSkuStateList.size() > 0) {
                prodIndexX.setState(appSkuStateList.get(0).getState().toString());
            }
        });
    }

    public void getProductXClientBySkuCode(AoyiProdIndexX prodIndexX, String renterId, String skuCode) {
        setRenterStateByMpu(renterId, prodIndexX);
        // 图片全路径
        updateImage(prodIndexX) ;
        if ("1".equals(prodIndexX.getState())) {
            // 设置租户价格
            setRenterPriceByMpu(renterId, prodIndexX);
            // 添加property
            setPropertyList(prodIndexX);
            if (StringUtils.isNotBlank(skuCode)) {
                List<StarSku> starSkus = starSkuDao.selectByCode(skuCode) ;
                if (starSkus != null && starSkus.size() > 0) {
                    StarSku starSku = starSkus.get(0) ;

                    com.fengchao.product.aoyi.bean.StarSkuBean starSkuBean = new com.fengchao.product.aoyi.bean.StarSkuBean() ;
                    BeanUtils.copyProperties(starSku, starSkuBean);
                    // 租户价格列表
                    List<AppSkuPrice> appSkuPrices = getAppSkuPriceListByStarSku(renterId,starSku) ;
                    if (appSkuPrices != null && appSkuPrices.size() > 0) {
                        starSku.setPrice(appSkuPrices.get(0).getPrice().intValue());
                    }
                    // 租户状态列表
                    List<AppSkuState> appSkuStates =  getAppSkuStateListByStarSku(renterId, starSku) ;
                    if (appSkuStates != null && appSkuStates.size() > 0) {
                        starSku.setStatus(appSkuStates.get(0).getState());
                    }
                    List<StarProperty> skuProperties = starPropertyDao.selectByProductIdAndType(starSku.getId(), 1) ;
                    starSkuBean.setPropertyList(skuProperties);
                    prodIndexX.setStarSku(starSkuBean);
                }
            }
        }
    }

    /**
     * 为MPU 添加 SKU
     * @param prodIndexX 源MPU
     * @param renterId  租户ID
     */
    private void addStarSkuList(AoyiProdIndexX prodIndexX, String renterId) {
        if (prodIndexX.getType() == 2) {
            // 添加 star sku
            prodIndexX.setSkuList(getStarSkuListByMpu(prodIndexX.getSkuid(), renterId));
            List<com.fengchao.product.aoyi.bean.StarSkuBean> starSkus = prodIndexX.getSkuList() ;

            // 获取最小值
            Optional<com.fengchao.product.aoyi.bean.StarSkuBean> starSkuOpt= starSkus.stream().min(Comparator.comparingInt(com.fengchao.product.aoyi.bean.StarSkuBean::getPrice));
            com.fengchao.product.aoyi.bean.StarSkuBean starSkuBean = starSkuOpt.get() ;
            prodIndexX.setStarSku(starSkuBean);
            BigDecimal bigDecimalPrice = new BigDecimal(starSkuBean.getPrice());
            BigDecimal bigDecimalSprice = new BigDecimal(starSkuBean.getSprice());
            prodIndexX.setPrice(bigDecimalPrice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
            prodIndexX.setSprice(bigDecimalSprice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
        }
        // 租户价格列表
        prodIndexX.setAppSkuPriceList(getAppSkuPriceListByMpu(renterId, prodIndexX.getMpu()));
        // 租户状态列表
        prodIndexX.setAppSkuStateList(getAppSkuStateListByMpu(renterId, prodIndexX.getMpu()));
    }

    public void batchAddStarSkuList(List<AoyiProdIndexX> prodIndexXES, String renterId) {
        // TODO 管理端批量添加
        List<String> skuIds = prodIndexXES.stream().filter(prodIndexX -> prodIndexX.getType() == 2).map(prodIndexX -> prodIndexX.getSkuid()).collect(Collectors.toList());
        List<String> mpuIds = prodIndexXES.stream().filter(prodIndexX -> prodIndexX.getType() != 2).map(prodIndexX -> prodIndexX.getMpu()).collect(Collectors.toList());
        // 租户价格列表
        List<AppSkuPrice> appSkuPrices = appSkuPriceDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpuIds, mpuIds);
        List<AppSkuState> appSkuStates = appSkuStateDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpuIds, mpuIds);
        // get all star sku bean
        List<StarSkuBean> starSkuBeans = batchGetStarSkuListByMpu(skuIds, renterId);

        // set star sku bean
        prodIndexXES.forEach(prodIndexX -> {
            if (prodIndexX.getType() == 2) {
                List<StarSkuBean> starSkuBeanList = new ArrayList<>();
                for (int i = 0; i < starSkuBeans.size(); i++) {
                    StarSkuBean starSkuBean = starSkuBeans.get(i);
                    if (prodIndexX.getSkuid().equals(starSkuBean.getSpuId())) {
                        starSkuBeanList.add(starSkuBean) ;
                        starSkuBeans.remove(i) ;
                        i = i - 1 ;
                    }
                }
                if (starSkuBeanList != null && starSkuBeanList.size() > 0) {
                    prodIndexX.setSkuList(starSkuBeanList);
                }
            }
            // set prod sku price
            List<AppSkuPrice> appSkuPriceList = new ArrayList<>();
            for (int i = 0; i < appSkuPrices.size(); i++) {
                AppSkuPrice appSkuPrice = appSkuPrices.get(i);
                if (prodIndexX.getMpu().equals(appSkuPrice.getMpu())) {
                    appSkuPriceList.add(appSkuPrice) ;
                    appSkuPrices.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuPriceList != null && appSkuPriceList.size() > 0) {
                prodIndexX.setAppSkuPriceList(appSkuPriceList);
            }
            // set prod sku state
            List<AppSkuState> appSkuStateList = new ArrayList<>();
            for (int i = 0; i < appSkuStates.size(); i++) {
                AppSkuState appSkuState = appSkuStates.get(i);
                if (prodIndexX.getMpu().equals(appSkuState.getMpu())) {
                    appSkuStateList.add(appSkuState) ;
                    appSkuStates.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuStateList != null && appSkuStateList.size() > 0) {
                prodIndexX.setAppSkuStateList(appSkuStateList);
            }
        });


//        if (prodIndexX.getType() == 2) {
//            // 添加 star sku
//            prodIndexX.setSkuList(batchGetStarSkuListByMpu(prodIndexX.getSkuid(), renterId));
//            List<com.fengchao.product.aoyi.bean.StarSkuBean> starSkus = prodIndexX.getSkuList() ;
//
//            // 获取最小值
//            Optional<com.fengchao.product.aoyi.bean.StarSkuBean> starSkuOpt= starSkus.stream().min(Comparator.comparingInt(com.fengchao.product.aoyi.bean.StarSkuBean::getPrice));
//            com.fengchao.product.aoyi.bean.StarSkuBean starSkuBean = starSkuOpt.get() ;
//            prodIndexX.setStarSku(starSkuBean);
//            BigDecimal bigDecimalPrice = new BigDecimal(starSkuBean.getPrice());
//            BigDecimal bigDecimalSprice = new BigDecimal(starSkuBean.getSprice());
//            prodIndexX.setPrice(bigDecimalPrice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
//            prodIndexX.setSprice(bigDecimalSprice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
//        }
    }

    /**
     * 为MPU 添加 SKU
     * @param prodIndexX 源MPU
     * @param renterId  租户ID
     */
    private void addStarSkuListClient(AoyiProdIndexX prodIndexX, String renterId) {
        if (prodIndexX.getType() == 2) {
            // 添加 star sku
            prodIndexX.setSkuList(getStarSkuListByMpuForClient(prodIndexX.getSkuid(), renterId));
            List<com.fengchao.product.aoyi.bean.StarSkuBean> starSkus = prodIndexX.getSkuList() ;

            // 获取最小值
            Optional<com.fengchao.product.aoyi.bean.StarSkuBean> starSkuOpt= starSkus.stream().min(Comparator.comparingInt(com.fengchao.product.aoyi.bean.StarSkuBean::getPrice));
            com.fengchao.product.aoyi.bean.StarSkuBean starSkuBean = starSkuOpt.get() ;
            prodIndexX.setStarSku(starSkuBean);
            BigDecimal bigDecimalPrice = new BigDecimal(starSkuBean.getPrice());
            BigDecimal bigDecimalSprice = new BigDecimal(starSkuBean.getSprice());
            prodIndexX.setPrice(bigDecimalPrice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
            prodIndexX.setSprice(bigDecimalSprice.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
        }
    }

    /**
     * 根据Mpu中的skuId获取star sku 列表
     * @param skuId sku
     * @param renterId 租户ID
     * @return  star sku 列表
     */
    private List<StarSkuBean> getStarSkuListByMpu(String skuId, String renterId) {
        List<StarSkuBean> starSkuBeans = new ArrayList<>() ;
        List<StarSku> starSkus = starSkuDao.selectBySpuId(skuId) ;
        if (starSkus != null && starSkus.size() >0) {
            starSkus.forEach(starSku -> {
                StarSkuBean starSkuBean = new StarSkuBean() ;
                BeanUtils.copyProperties(starSku, starSkuBean);
                // 租户价格列表
                starSkuBean.setAppSkuPriceList(getAppSkuPriceListByStarSku(renterId,starSku));
                // 租户状态列表
                starSkuBean.setAppSkuStateList(getAppSkuStateListByStarSku(renterId, starSku));
                List<StarProperty> skuProperties = starPropertyDao.selectByProductIdAndType(starSku.getId(), 1) ;
                starSkuBean.setPropertyList(skuProperties);
                starSkuBeans.add(starSkuBean) ;
            });
        }
        return starSkuBeans ;
    }

    private List<StarSkuBean> batchGetStarSkuListByMpu(List<String> skuIds, String renterId) {
        if (skuIds != null && skuIds.size() > 0) {
            List<StarSku> starSkus = starSkuDao.selectBySpuIds(skuIds, -1) ;
            List<StarSkuBean> starSkuBeans = starSkus.stream().map(starSku -> {
                StarSkuBean starSkuBean = new StarSkuBean() ;
                BeanUtils.copyProperties(starSku, starSkuBean);
                return starSkuBean;
            }).collect(Collectors.toList());
            List<String> mpus = starSkus.stream().map(starSku -> starSku.getSpuId()).distinct().collect(Collectors.toList());
            List<String> codes = starSkus.stream().map(starSku -> starSku.getCode()).distinct().collect(Collectors.toList());
            List<Integer> ids = starSkus.stream().map(starSku -> starSku.getId()).collect(Collectors.toList());
            // 批量租户价格列表
            List<AppSkuPrice> appSkuPrices = appSkuPriceDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpus, codes) ;
            // 批量租户状态列表
            List<AppSkuState> appSkuStates = appSkuStateDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpus, codes) ;
            // 批量属性列表
            List<StarProperty> skuProperties = starPropertyDao.selectByProductIdsAndType(ids,1) ;
            starSkuBeans.forEach(starSkuBean -> {
                // set property
                List<StarProperty> starProperties = new ArrayList<>();
                for (int i = 0; i < skuProperties.size(); i++) {
                    StarProperty starProperty = skuProperties.get(i);
                    if (starSkuBean.getId() == starProperty.getProductId()) {
                        starProperties.add(starProperty);
                        skuProperties.remove(i) ;
                        i = i - 1;
                    }
                }
                if (starProperties != null && starProperties.size() > 0) {
                    starSkuBean.setPropertyList(starProperties);
                }
                // set app sku price
                List<AppSkuPrice> appSkuPriceList = new ArrayList<>();
                for (int i = 0; i < appSkuPrices.size(); i++) {
                    AppSkuPrice appSkuPrice = appSkuPrices.get(i);
                    if (starSkuBean.getSpuId().equals(appSkuPrice.getMpu()) && starSkuBean.getCode().equals(appSkuPrice.getSkuId())) {
                        appSkuPriceList.add(appSkuPrice);
                        appSkuPrices.remove(i);
                        i = i - 1;
                    }
                }
                if (appSkuPriceList != null && appSkuPriceList.size() > 0) {
                    starSkuBean.setAppSkuPriceList(appSkuPriceList);
                }
                // set app sku state
                List<AppSkuState> appSkuStateList = new ArrayList<>();
                for (int i = 0; i < appSkuStates.size(); i++) {
                    AppSkuState appSkuState = appSkuStates.get(i);
                    if (starSkuBean.getSpuId().equals(appSkuState.getMpu()) && starSkuBean.getCode().equals(appSkuState.getSkuId())) {
                        appSkuStateList.add(appSkuState);
                        appSkuStates.remove(i);
                        i = i - 1;
                    }
                }
                if (appSkuStateList != null && appSkuStateList.size() > 0) {
                    starSkuBean.setAppSkuStateList(appSkuStateList);
                }
            });
            return starSkuBeans ;
        } else {
            return new ArrayList<>() ;
        }

    }

    /**
     * 客户端批量添加star sku
     * @param skuIds
     * @param renterId
     * @return
     */
    private List<StarSkuBean> batchGetStarSkuListByMpuClient(List<String> skuIds, String renterId) {
        // TODO 客户端批量添加star sku
        List<StarSku> starSkus = starSkuDao.selectBySpuIds(skuIds, 1) ;
        List<StarSkuBean> starSkuBeans = starSkus.stream().map(starSku -> {
            StarSkuBean starSkuBean = new StarSkuBean() ;
            BeanUtils.copyProperties(starSku, starSkuBean);
            return starSkuBean;
        }).collect(Collectors.toList());
        List<String> mpus = starSkus.stream().map(starSku -> starSku.getSpuId()).distinct().collect(Collectors.toList());
        List<String> codes = starSkus.stream().map(starSku -> starSku.getCode()).distinct().collect(Collectors.toList());
        List<Integer> ids = starSkus.stream().map(starSku -> starSku.getId()).collect(Collectors.toList());
        // 批量租户价格列表
        List<AppSkuPrice> appSkuPrices = appSkuPriceDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpus, codes) ;
        // 批量租户状态列表
        List<AppSkuState> appSkuStates = appSkuStateDao.batchSelectByRenterIdAndMpuAndSku(renterId, mpus, codes) ;
        // 批量属性列表
        List<StarProperty> skuProperties = starPropertyDao.selectByProductIdsAndType(ids,1) ;
        starSkuBeans.forEach(starSkuBean -> {
            // set property
            List<StarProperty> starProperties = new ArrayList<>();
            for (int i = 0; i < skuProperties.size(); i++) {
                StarProperty starProperty = skuProperties.get(i);
                if (starSkuBean.getId() == starProperty.getProductId()) {
                    starProperties.add(starProperty);
                    skuProperties.remove(i) ;
                    i = i - 1;
                }
            }
            if (starProperties != null && starProperties.size() > 0) {
                starSkuBean.setPropertyList(starProperties);
            }
            // set app sku price
            List<AppSkuPrice> appSkuPriceList = new ArrayList<>();
            for (int i = 0; i < appSkuPrices.size(); i++) {
                AppSkuPrice appSkuPrice = appSkuPrices.get(i);
                if (starSkuBean.getSpuId().equals(appSkuPrice.getMpu()) && starSkuBean.getCode().equals(appSkuPrice.getSkuId())) {
                    appSkuPriceList.add(appSkuPrice);
                    appSkuPrices.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuPriceList != null && appSkuPriceList.size() > 0) {
                starSkuBean.setPrice(appSkuPriceList.get(0).getPrice().intValue());
            }
            // set app sku state
            List<AppSkuState> appSkuStateList = new ArrayList<>();
            for (int i = 0; i < appSkuStates.size(); i++) {
                AppSkuState appSkuState = appSkuStates.get(i);
                if (starSkuBean.getSpuId().equals(appSkuState.getMpu()) && starSkuBean.getCode().equals(appSkuState.getSkuId())) {
                    appSkuStateList.add(appSkuState);
                    appSkuStates.remove(i);
                    i = i - 1;
                }
            }
            if (appSkuStateList != null && appSkuStateList.size() > 0) {
                starSkuBean.setStatus(appSkuStateList.get(0).getState());
            }
        });
        return starSkuBeans ;
    }

    /**
     * 根据Mpu中的skuId获取star sku 列表
     * @param skuId sku
     * @param renterId 租户ID
     * @return  star sku 列表
     */
    public List<StarSkuBean> getStarSkuListByMpuForClient(String skuId, String renterId) {
        List<StarSkuBean> starSkuBeans = new ArrayList<>() ;
        List<StarSku> starSkus = starSkuDao.selectBySpuId(skuId) ;
        if (starSkus != null && starSkus.size() >0) {
            starSkus.forEach(starSku -> {
                StarSkuBean starSkuBean = new StarSkuBean() ;
                BeanUtils.copyProperties(starSku, starSkuBean);
                // 租户价格列表
                List<AppSkuPrice> appSkuPrices = getAppSkuPriceListByStarSku(renterId,starSku) ;
                if (appSkuPrices != null && appSkuPrices.size() > 0) {
                    starSku.setPrice(appSkuPrices.get(0).getPrice().intValue());
                }
                // 租户状态列表
                List<AppSkuState> appSkuStates =  getAppSkuStateListByStarSku(renterId, starSku) ;
                if (appSkuStates != null && appSkuStates.size() > 0) {
                    starSku.setStatus(appSkuStates.get(0).getState());
                }
                List<StarProperty> skuProperties = starPropertyDao.selectByProductIdAndType(starSku.getId(), 1) ;
                starSkuBean.setPropertyList(skuProperties);
                starSkuBeans.add(starSkuBean) ;
            });
        }
        return starSkuBeans ;
    }



    /**
     * 根据star sku获取租户价格列表
     * @param renterId  租户ID
     * @param starSku   sku
     * @return  租户价格列表
     */
    private List<AppSkuPrice> getAppSkuPriceListByStarSku(String renterId, StarSku starSku) {
        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
        appSkuPrice.setRenterId(renterId);
        appSkuPrice.setMpu(starSku.getSpuId());
        appSkuPrice.setSkuId(starSku.getCode());
        return appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
    }

    /**
     * 根据MPU获取租户价格列表
     * @param renterId 租户ID
     * @param mpu mpu
     * @return 租户价格列表
     */
    private List<AppSkuPrice> getAppSkuPriceListByMpu(String renterId, String mpu) {
        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
        appSkuPrice.setRenterId(renterId);
        appSkuPrice.setMpu(mpu);
        appSkuPrice.setSkuId(mpu);
        return appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
    }

    /**
     * 设置租户商品价格
     * @param renterId  租户ID
     * @param prodIndexX   商品详情
     */
    private void setRenterPriceByMpu(String renterId, AoyiProdIndexX prodIndexX) {
        AppSkuPrice appSkuPrice = new AppSkuPrice() ;
        appSkuPrice.setRenterId(renterId);
        appSkuPrice.setMpu(prodIndexX.getMpu());
        appSkuPrice.setSkuId(prodIndexX.getMpu());
        List<AppSkuPrice> appSkuPriceList = appSkuPriceDao.selectByRenterIdAndMpuAndSku(appSkuPrice) ;
        if (appSkuPriceList != null && appSkuPriceList.size() > 0) {
            appSkuPrice = appSkuPriceList.get(0);
            BigDecimal bigDecimalPrice = appSkuPrice.getPrice().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP) ;
            prodIndexX.setPrice(bigDecimalPrice.toString());
        }
    }

    /**
     * 设置租户商品状态
     * @param renterId 租户ID
     * @param prodIndexX   商品详情
     */
    private void setRenterStateByMpu(String renterId, AoyiProdIndexX prodIndexX) {
        AppSkuState bean = new AppSkuState() ;
        bean.setRenterId(renterId);
        bean.setMpu(prodIndexX.getMpu());
        bean.setSkuId(prodIndexX.getMpu());
        List<AppSkuState> list = appSkuStateDao.selectByRenterIdAndMpuAndSku(bean) ;
        if (list != null && list.size() > 0) {
            bean = list.get(0);
            prodIndexX.setState(bean.getState().toString());
        }
    }

    /**
     * 根据star sku获取租户上下架状态列表
     * @param renterId  租户ID
     * @param starSku   sku
     * @return  租户上下架状态列表
     */
    private List<AppSkuState> getAppSkuStateListByStarSku(String renterId, StarSku starSku) {
        AppSkuState appSkuState = new AppSkuState() ;
        appSkuState.setRenterId(renterId);
        appSkuState.setMpu(starSku.getSpuId());
        appSkuState.setSkuId(starSku.getCode());
        return appSkuStateDao.selectByRenterIdAndMpuAndSku(appSkuState) ;
    }



    /**
     * 根据MPU获取租户上下架状态列表
     * @param renterId 租户ID
     * @param mpu mpu
     * @return 租户上下架状态列表
     */
    private List<AppSkuState> getAppSkuStateListByMpu(String renterId, String mpu) {
        AppSkuState appSkuState = new AppSkuState() ;
        appSkuState.setRenterId(renterId);
        appSkuState.setMpu(mpu);
        appSkuState.setSkuId(mpu);
        return appSkuStateDao.selectByRenterIdAndMpuAndSku(appSkuState) ;
    }

    /**
     * 设置商品property
     * @param prodIndexX 商品详情
     */
    private void setPropertyList(AoyiProdIndexX prodIndexX) {
        if (prodIndexX.getType() == 2) {
            List<StarProperty> starProperties = starPropertyDao.selectByProductIdAndType(prodIndexX.getId(), 0) ;
            prodIndexX.setProperties(starProperties);
        }
    }

    /**
     * 根据平台权限设置merchant list 参数
     * @param queryBean  产品查询条件
     */
    public void setMerchantListForProductQueryBean(ProductQueryBean queryBean) {
        log.info("setMerchantListForProductQueryBean 入参：{}",JSONUtil.toJsonString(queryBean));
        List<Integer> merchantIds = null ;
        if ("0".equals(queryBean.getRenterHeader())) {
            // 平台管理员
            // 获取所有租户下的所有商户信息
            if (queryBean.getMerchantHeader() == 0) {
                if (StringUtils.isNotBlank(queryBean.getAppId())) {
                    merchantIds = vendorsRpcService.queryMerhantListByAppId(queryBean.getAppId()) ;
                } else {
                    if (StringUtils.isNotBlank(queryBean.getRenterId())) {
                        merchantIds = vendorsRpcService.queryRenterMerhantList(queryBean.getRenterId()) ;
                    } else {
                        merchantIds = vendorsRpcService.queryRenterMerhantList("") ;
                    }
                }
                if (merchantIds == null || merchantIds.size() == 0) {
                    queryBean = null ;
                }
                //  判断商户中是否存在merchantId
                if (merchantIds.contains(queryBean.getMerchantId()))  {
                    queryBean.setMerchantIds(null);
                } else {
                    queryBean.setMerchantIds(merchantIds);
                }
            } else {
                queryBean.setMerchantId(queryBean.getMerchantHeader());
            }
        } else {
            // 租户
            if (queryBean.getMerchantHeader() == 0) {
                // 获取当前租户下的所有商户信息
                if (StringUtils.isNotBlank(queryBean.getAppId())) {
                    merchantIds = vendorsRpcService.queryMerhantListByAppId(queryBean.getAppId()) ;
                } else {
                    merchantIds = vendorsRpcService.queryRenterMerhantList(queryBean.getRenterHeader()) ;
                }
                if (merchantIds == null || merchantIds.size() == 0) {
                    queryBean = null;
                }
                queryBean.setMerchantIds(merchantIds);
                // 查询租户的类目
                HashMap map = new HashMap();
                map.put("renterId", queryBean.getRenterHeader()) ;
                map.put("categoryClass", 3) ;
                List<AoyiBaseCategoryX> categoryXES = categoryXMapper.selectRenterCategoryByRenterId(map) ;
                List<String> categories = categoryXES.stream().map(c -> c.getCategoryId().toString()).collect(Collectors.toList()) ;
                queryBean.setCategories(categories);
            } else {
                // 租户的商户
                merchantIds = vendorsRpcService.queryRenterMerhantList(queryBean.getRenterHeader()) ;
                if (merchantIds.contains(queryBean.getMerchantHeader())) {
                    queryBean.setMerchantId(queryBean.getMerchantHeader());
                }
            }
        }
        log.info("setMerchantListForProductQueryBean  返回值：{}", JSONUtil.toJsonString(queryBean));
    }

    /**
     * 客户端获取商户相关信息并设置产品查询bean
     * @param queryBean 产品查询bean
     */
    public void setClientProductQueryBean(ProductQueryBean queryBean) {
        log.info("setClientProductQueryBean 入参：{}", JSONUtil.toJsonString(queryBean));
        String renterId = vendorsRpcService.queryRenterId(queryBean.getAppId()) ;
        queryBean.setRenterId(renterId);
        if (StringUtils.isBlank(queryBean.getCategory())) {
            // 设置类别
            HashMap map = new HashMap();
            map.put("appId", queryBean.getAppId()) ;
            map.put("categoryClass", "3");
            List<AoyiBaseCategoryX> categoryXES = categoryXMapper.selectRenterCategoryByAppId(map) ;
            List<String> categories = categoryXES.stream().map(c -> String.valueOf(c.getCategoryId())).collect(Collectors.toList()) ;
            queryBean.setCategories(categories);
        }
        // 获取可读取的商户配置
        List<Integer> merchantIds = vendorsRpcService.queryMerhantListByAppId(queryBean.getAppId()) ;
        queryBean.setMerchantIds(merchantIds);
        MerchantCodeBean merchantCodeBean = getMerchantCodesByAppId(queryBean.getAppId()) ;
        List<String> codes = new ArrayList<>() ;
        if (merchantCodeBean != null) {
            codes = merchantCodeBean.getCodes() ;
        }
        queryBean.setMerchantCodes(codes);
        log.info("setClientProductQueryBean  返回值：{}", JSONUtil.toJsonString(queryBean));
    }

    /**
     * 配置文件中获取端的 code配置
     * @param appId
     * @return
     */
    private MerchantCodeBean getMerchantCodesByAppId(String appId) {
        log.info(JSONUtil.toJsonString(config.getRegion()));
        return  config.getRegion().get(appId) ;
    }
}
