package com.fengchao.order.service.impl;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.supply.SupplyOrderBean;
import com.fengchao.order.bean.supply.SupplySkuBean;
import com.fengchao.order.service.SupplyOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author songbw
 * @date 2021/1/18 16:32
 */
@Slf4j
@Service
public class SupplyOrderServiceImpl implements SupplyOrderService {



    @Override
    public OperaResponse preOrder(SupplyOrderBean supplyOrderBean) {
        OperaResponse response = new OperaResponse() ;
        if (supplyOrderBean == null) {
            response.setCode(1000000);
            response.setMsg("数据为null");
            return response ;
        }
        List<SupplySkuBean> supplySkuBeanList = supplyOrderBean.getSkuList() ;
        if (supplySkuBeanList == null || supplySkuBeanList.size() == 0) {
            response.setCode(1000000);
            response.setMsg("sku列表为null");
            return response ;
        }

        // TODO 根据merchantCode进行拆单
        // TODO 验证运费  验证总金额是否正确
        // TODO 请求各服务，验证是否有货,并下单
        // TODO 添加主订单，添加主订单

        return response;
    }
}
