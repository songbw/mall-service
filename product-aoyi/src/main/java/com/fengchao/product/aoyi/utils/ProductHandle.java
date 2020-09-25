package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.StarSkuBean;
import com.fengchao.product.aoyi.dao.AppSkuPriceDao;
import com.fengchao.product.aoyi.dao.AppSkuStateDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductHandle {

    private StarSkuDao starSkuDao ;
    private AppSkuPriceDao appSkuPriceDao ;
    private AppSkuStateDao appSkuStateDao ;

    @Autowired
    public ProductHandle(StarSkuDao starSkuDao, AppSkuPriceDao appSkuPriceDao, AppSkuStateDao appSkuStateDao) {
        this.starSkuDao = starSkuDao;
        this.appSkuPriceDao = appSkuPriceDao;
        this.appSkuStateDao = appSkuStateDao;
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
                aoyiProdIndex = updateImageExample(aoyiProdIndex) ;
                AoyiProdIndexX aoyiProdIndexX = new AoyiProdIndexX() ;
                BeanUtils.copyProperties(aoyiProdIndex, aoyiProdIndexX);
                // 添加star sku列表
                addStarSkuList(aoyiProdIndexX, queryBean) ;
                prodIndexXList.add(aoyiProdIndexX) ;
            });
        }
        return prodIndexXList ;
    }

    /**
     * 为MPU 添加 SKU
     * @param prodIndexX 源MPU
     * @param queryBean  查询条件
     */
    private void addStarSkuList(AoyiProdIndexX prodIndexX, ProductQueryBean queryBean) {
        if (!prodIndexX.getSkuid().equals(prodIndexX.getMpu())) {
            // 添加 star sku
            prodIndexX.setSkuList(getStarSkuListByMpu(prodIndexX.getSkuid(), queryBean.getRenterId()));
        } else {
            // 租户价格列表
            prodIndexX.setAppSkuPriceList(getAppSkuPriceListByMpu(queryBean.getRenterId(), prodIndexX.getMpu()));
            // 租户状态列表
            prodIndexX.setAppSkuStateList(getAppSkuStateListByMpu(queryBean.getRenterId(), prodIndexX.getMpu()));
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
}
