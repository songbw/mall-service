package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.StarSkuBean;
import com.fengchao.product.aoyi.config.ESConfig;
import com.fengchao.product.aoyi.config.MerchantCodeBean;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    public ProductHandle(StarSkuDao starSkuDao, AppSkuPriceDao appSkuPriceDao, AppSkuStateDao appSkuStateDao, StarDetailImgDao starDetailImgDao,StarPropertyDao starPropertyDao, VendorsRpcService vendorsRpcService, ESConfig config) {
        this.starSkuDao = starSkuDao;
        this.appSkuPriceDao = appSkuPriceDao;
        this.appSkuStateDao = appSkuStateDao;
        this.starDetailImgDao = starDetailImgDao;
        this.starPropertyDao = starPropertyDao ;
        this.vendorsRpcService = vendorsRpcService;
        this.config = config ;
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
        List<AoyiProdIndexX> prodIndexXList = new ArrayList<>() ;
        if (prodIndexList != null) {
            prodIndexList.forEach(aoyiProdIndex -> {
                // 获取全部商品信息
                prodIndexXList.add(convertProductXByProd(aoyiProdIndex, queryBean.getRenterId())) ;
            });
        }
        return prodIndexXList ;
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
     * 为MPU 添加 SKU
     * @param prodIndexX 源MPU
     * @param renterId  租户ID
     */
    private void addStarSkuList(AoyiProdIndexX prodIndexX, String renterId) {
        if (prodIndexX.getType() == 2) {
            // 添加 star sku
            prodIndexX.setSkuList(getStarSkuListByMpu(prodIndexX.getSkuid(), renterId));
        } else {
            // 租户价格列表
            prodIndexX.setAppSkuPriceList(getAppSkuPriceListByMpu(renterId, prodIndexX.getMpu()));
            // 租户状态列表
            prodIndexX.setAppSkuStateList(getAppSkuStateListByMpu(renterId, prodIndexX.getMpu()));
        }
    }

    /**
     * 为MPU 添加 SKU
     * @param prodIndexX 源MPU
     * @param renterId  租户ID
     */
    private void addStarSku(AoyiProdIndexX prodIndexX, String renterId) {
        if (prodIndexX.getType() == 2) {
            // 添加 star sku
            prodIndexX.setSkuList(getStarSkuListByMpu(prodIndexX.getSkuid(), renterId));
        } else {
            // 租户价格列表
            prodIndexX.setAppSkuPriceList(getAppSkuPriceListByMpu(renterId, prodIndexX.getMpu()));
            // 租户状态列表
            prodIndexX.setAppSkuStateList(getAppSkuStateListByMpu(renterId, prodIndexX.getMpu()));
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
            prodIndexX.setPrice(appSkuPrice.getPrice().toString());
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
        log.info("setMerchantListForProductQueryBean 入参：{}",queryBean);
        List<Integer> merchantIds = null ;
        if ("0".equals(queryBean.getRenterHeader())) {
            // 平台管理员
            // 获取所有租户下的所有商户信息
            if (StringUtils.isNotBlank(queryBean.getAppId())) {
                merchantIds = vendorsRpcService.queryMerhantListByAppId(queryBean.getAppId()) ;
            } else {
                if (StringUtils.isNotBlank(queryBean.getRenterId())) {
                    merchantIds = vendorsRpcService.queryRenterMerhantList(queryBean.getRenterId()) ;
                } else {
                    merchantIds = vendorsRpcService.queryRenterMerhantList("") ;
                }
            }
            //  判断商户中是否存在merchantId
            if (merchantIds.contains(queryBean.getMerchantId()))  {
                queryBean.setMerchantIds(null);
            } else {
                queryBean.setMerchantIds(merchantIds);
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
                queryBean.setMerchantIds(merchantIds);
            } else {
                // 租户的商户
                merchantIds = vendorsRpcService.queryRenterMerhantList(queryBean.getRenterHeader()) ;
                if (merchantIds.contains(queryBean.getMerchantHeader())) {
                    queryBean.setMerchantId(queryBean.getMerchantHeader());
                }
            }
        }
        log.info("setMerchantListForProductQueryBean  返回值：{}", queryBean);
    }

    /**
     * 客户端获取商户相关信息并设置产品查询bean
     * @param queryBean 产品查询bean
     */
    public void setClientProductQueryBean(ProductQueryBean queryBean) {
        String renterId = vendorsRpcService.queryRenterId(queryBean.getAppId()) ;
        queryBean.setRenterId(renterId);
        // 获取可读取的商户配置
        List<Integer> merchantIds = vendorsRpcService.queryMerhantListByAppId(queryBean.getAppId()) ;
        queryBean.setMerchantIds(merchantIds);
        MerchantCodeBean merchantCodeBean = getMerchantCodesByAppId(queryBean.getAppId()) ;
        List<String> codes = new ArrayList<>() ;
        if (merchantCodeBean != null) {
            codes = merchantCodeBean.getCodes() ;
        }
        queryBean.setMerchantCodes(codes);
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
