package com.fengchao.order.service;

import com.fengchao.order.bean.AddressCodeBean;
import com.fengchao.order.bean.AddressQueryBean;
import com.fengchao.order.bean.GPSQueryBean;
import com.fengchao.order.model.AoyiBaseFulladdress;

import java.util.List;

/**
 * 地址服务
 */
public interface AddressService {

    List<AoyiBaseFulladdress> findByLevel(AddressQueryBean queryBean) ;

    AddressCodeBean findCode(GPSQueryBean queryBean) ;

}
