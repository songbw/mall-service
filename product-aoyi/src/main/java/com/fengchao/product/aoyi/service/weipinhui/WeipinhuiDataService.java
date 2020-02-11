package com.fengchao.product.aoyi.service.weipinhui;

import java.util.List;

/**
 * 用于同步唯品会数据的服务
 */
public interface WeipinhuiDataService {

    /**
     * 同步品牌
     *
     * @param pageNumber
     * @param maxPageNumber 最大查询的页数，-1为无限
     * @throws Exception
     */
    void syncGetBrand(Integer pageNumber, Integer maxPageNumber) throws Exception;

    /**
     * 同步品牌
     *
     * @param pageNumber
     * @param maxPageNumber 最大查询的页数，-1为无限
     * @throws Exception
     */
    void syncGetCategory(Integer pageNumber, Integer maxPageNumber) throws Exception;

    /**
     * 同步spu和sku
     *
     * @param itemIdList
     * @param maxCount 同步最大的itemId的数量，当达到maxCount数量时，则停止同步，-1为无限
     * @throws Exception
     */
    void syncItemDetail(List<String> itemIdList, Integer maxCount) throws Exception;
}
