package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2021/1/27 7:12
 */
@Setter
@Getter
public class KioskQueryBean extends QueryBean {
    private String kioskId;
    private String kioskNo;
    private String location ;
}
