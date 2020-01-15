package com.fengchao.aoyi.client.bean.dto.weipinhui;


import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: labanYB
 * @Date: 2019/6/14 0014 15:43
 * @Description: brand class
 */
@Setter
@Getter
public class AoyiProdRquest extends BaseRquestParam {

    //当前页码
    private Integer pageNumber;

    //每页品牌数量
    private Integer pageSize;

}