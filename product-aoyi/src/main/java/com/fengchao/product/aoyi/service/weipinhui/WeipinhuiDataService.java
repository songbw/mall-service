package com.fengchao.product.aoyi.service.weipinhui;

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
}
