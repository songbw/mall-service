package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.*;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.feign.ProductService;
import com.fengchao.statistics.mapper.MerchantOverviewMapper;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.service.MerchantOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantOverviewServiceImpl implements MerchantOverviewService {

    @Autowired
    private MerchantOverviewMapper mapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @Override
    public void add(QueryBean queryBean) {
        List<MerchantPaymentBean> merchantPaymentBeans = getMerchantCount(queryBean) ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = null ;
        try {
            date = formatter.parse(queryBean.getStartTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date createDate = new Date() ;
        Date finalDate = date;
        merchantPaymentBeans.forEach(merchantPaymentBean -> {
            MerchantOverview merchantOverview = new MerchantOverview();
            merchantOverview.setCreatedAt(createDate);
            merchantOverview.setStatisticsDate(finalDate);
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

    private List<MerchantPaymentBean> getMerchantCount(QueryBean queryBean) {
        OperaResult result = orderService.paymentMerchantCount(queryBean.getStartTime(), queryBean.getEndTime());
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<MerchantPaymentBean> merchantPaymentBeans = JSONObject.parseArray(jsonString, MerchantPaymentBean.class) ;
            return merchantPaymentBeans;
        }
        return null;
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
