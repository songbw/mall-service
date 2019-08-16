package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.model.AoyiProdIndexX;

public class ProductHandle {
    public static AoyiProdIndexX updateImage(AoyiProdIndexX aoyiProdIndexX) {
        String imageUrl = aoyiProdIndexX.getImagesUrl();
        if (aoyiProdIndexX.getImage() == null && "".equals(aoyiProdIndexX.getImage()) && imageUrl != null && (!"".equals(imageUrl))) {
            String image = "";
            if (imageUrl.indexOf("/") == 0) {
                image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
            } else {
                image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
            }
            aoyiProdIndexX.setImage(image);
        }
        return aoyiProdIndexX;
    }
}
