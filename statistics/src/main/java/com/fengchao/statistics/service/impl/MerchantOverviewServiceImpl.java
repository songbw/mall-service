package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.*;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.feign.ProductServiceClient;
import com.fengchao.statistics.mapper.MerchantOverviewMapper;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.service.MerchantOverviewService;
import com.fengchao.statistics.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MerchantOverviewServiceImpl implements MerchantOverviewService {

    @Autowired
    private MerchantOverviewMapper mapper;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductServiceClient productService;

    @Autowired
    private OrdersRpcService ordersRpcService;

    @Override
    public void doDailyStatistic(String startDateTime, String endDateTime) {
        // 1. 调用order rpc服务，根据时间范围查询已支付的订单详情
        List<OrderDetailBean> orderDetailBeanList = ordersRpcService.queryPayedOrderDetailList(startDateTime, endDateTime);

        // 2. 根据商户id维度将订单详情分类
        // 获取统计时间
        Date statisticDate = DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        merchantPaymentBeans.forEach(merchantPaymentBean -> {
            MerchantOverview merchantOverview = new MerchantOverview();
            merchantOverview.setStatisticsDate(statisticDate);
            merchantOverview.setOrderPaymentAmount(merchantPaymentBean.getSaleAmount());
            merchantOverview.setMerchantId(merchantPaymentBean.getMerchantId());
            // 查询商户信息
            SkuCode skuCode = getMerchantInfo(merchantPaymentBean.getMerchantId()) ;
            if (skuCode != null) {
                merchantOverview.setMerchantCode(skuCode.getMerchantCode());
                merchantOverview.setMerchantName(skuCode.getMerchantName());
            }
            //存库
            mapper.insertSelective(merchantOverview) ;
        });
    }

    @Override
    public List<MerchantOverview> findsum(QueryBean queryBean) {
        HashMap map = new HashMap() ;
        map.put("start", queryBean.getStartTime());
        map.put("end", queryBean.getEndTime()) ;
        return mapper.selectSum(map);
    }

    private SkuCode getMerchantInfo(int id) {
        OperaResult result = productService.findMerchant(id) ;
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            SkuCode skuCode = JSONObject.parseObject(jsonString, SkuCode.class) ;
            return skuCode;
        }
        return null;
    }

}
