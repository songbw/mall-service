package com.fengchao.aoyi.client.weipinhuiService;

import com.fengchao.aoyi.client.bean.dto.AoyiItemDetailResDto;
import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.bean.dto.CategoryResDto;

import java.util.List;

/**
 * 唯品会商品相关接口服务
 *
 */
public interface ProductWeipinhuiService {

    /**
     * 获取品牌列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<BrandResDto> getBrand(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 获取类目列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<CategoryResDto> getCategory(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 获取items列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<AoyiItemDetailResDto> queryItemsList(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 根据itemId查询详情
     *
     * @param itemId
     * @return
     */
    AoyiItemDetailResDto queryItemDetial(String itemId) throws Exception;
}
