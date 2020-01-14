package com.fengchao.aoyi.client.bean.dto;


import lombok.Data;

/**
 * @Auther: labanYB
 * @Date: 2019/6/14 0014 15:43
 * @Description: brand class
 */
@Data
public class AoyiProdRquest extends BaseRquestParam {

    //当前页码
    private Integer pageNumber;

    //每页品牌数量
    private Integer pageSize;

}