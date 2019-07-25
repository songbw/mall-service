package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.*;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.feign.ProductService;
import com.fengchao.statistics.mapper.MerchantOverviewMapper;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.service.MerchantOverviewService;
import com.fengchao.statistics.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private ProductService productService;

    @Autowired
    private OrdersRpcService ordersRpcService;

    @Override
    public void add(QueryBean queryBean) {
        // 1. 调用order rpc服务，获取统计数据
        List<MerchantPaymentBean> merchantPaymentBeans = ordersRpcService.statisticOrdersAmountByMerchant1(queryBean);

        // 2. 处理结果
        // 获取统计时间
        Date statisticDate = DateUtil.parseDateTime(queryBean.getStartTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        merchantPaymentBeans.forEach(merchantPaymentBean -> {
            MerchantOverview merchantOverview = new MerchantOverview();
            merchantOverview.setCreatedAt(new Date());
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
