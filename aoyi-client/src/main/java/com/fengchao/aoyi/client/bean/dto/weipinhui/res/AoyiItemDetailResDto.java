package com.fengchao.aoyi.client.bean.dto.weipinhui.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AoyiItemDetailResDto {

    private String itemId; // "50000550",

    /**
     * 商品名称
     */
    private String itemTitle; // "B.Toys 比乐 功能触感球套装感统玩具发声可啃咬 6 个月+",

    /**
     * 是否出售
     */
    private String canSell; // "false"

    /**
     * 品牌 id
     */
    private String brandId; // null

    /**
     * 类目三级 id
     */
    private String categoryId; // "50023439"

    /**
     * 商品主图
     */
    private String itemImage; // [http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/1.jpg,http://pic.aoyi365.com/aoyi_tmall/50 000550/ZT/2.jpg,http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/3.jpg,http://pic.aoyi365.co m/aoyi_tmall/50000550/ZT/4.jpg,]", // 商品主图

    /**
     * 商品详情图
     */
    private String itemDetailImage; // [http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/1.jpg,http://pic.aoyi365.com/aoyi_tmall/50 000550/ZT/2.jpg,http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/3.jpg,http://pic.aoyi365.co m/aoyi_tmall/50000550/ZT/4.jpg,]",//商品详情图

    /**
     * sku list
     */
    List<AoyiSkuResDto> aoyiSkusResponses;

}
