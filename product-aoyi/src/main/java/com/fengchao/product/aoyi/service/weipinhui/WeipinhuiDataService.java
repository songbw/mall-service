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
     * 同步itemIdList
     *
     * @param pageNumber
     * @param maxPageCount
     * @throws Exception
     */
    void syncItemIdList(Integer pageNumber, Integer maxPageCount) throws Exception;

    /**
     * 同步spu和sku
     *
     * @param pageNumber
     * @param syncItemDetail 最大查询的页数，-1为无限
     * @throws Exception
     */
    void syncItemDetail(Integer pageNumber, Integer syncItemDetail) throws Exception;

    /**
     * 用于star_property数据的修复
     *
     * @throws Exception
     */
    void fixStarProperty() throws Exception;
}
