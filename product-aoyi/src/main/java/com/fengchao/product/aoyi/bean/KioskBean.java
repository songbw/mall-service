package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskImg;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2021/2/1 9:16
 */
@Setter
@Getter
public class KioskBean extends Kiosk {
    private List<KioskImg> imgs ;
}
