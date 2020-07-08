package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.config.MerchantCodeBean;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.AoyiProdIndexXWithBLOBs;

public class ProductHandle {
    public static AoyiProdIndexX updateImage(AoyiProdIndexX aoyiProdIndexX) {
        if (aoyiProdIndexX.getImage() == null || "".equals(aoyiProdIndexX.getImage())) {
            String imageUrl = aoyiProdIndexX.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image = "";
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

    public static AoyiProdIndex updateImageExample(AoyiProdIndex aoyiProdIndex) {
        if (aoyiProdIndex.getImage() == null || "".equals(aoyiProdIndex.getImage())) {
            String imageUrl = aoyiProdIndex.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image = "";
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

    public static AoyiProdIndexXWithBLOBs updateImageWithBLOBS(AoyiProdIndexXWithBLOBs aoyiProdIndexX) {
        if (aoyiProdIndexX.getImage() == null || "".equals(aoyiProdIndexX.getImage())) {
            String imageUrl = aoyiProdIndexX.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image = "";
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
}
