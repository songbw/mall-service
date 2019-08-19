package com.fengchao.elasticsearch.utils;

import com.fengchao.elasticsearch.domain.AoyiProdIndex;

public class ProductHandle {
    public static AoyiProdIndex updateImage(AoyiProdIndex aoyiProdIndexX) {
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
