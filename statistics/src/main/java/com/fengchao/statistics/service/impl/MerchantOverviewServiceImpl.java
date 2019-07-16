package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.DayStatisticsBean;
import com.fengchao.statistics.bean.MerchantPaymentBean;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.mapper.MerchantOverviewMapper;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.service.MerchantOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MerchantOverviewServiceImpl implements MerchantOverviewService {

    @Autowired
    private MerchantOverviewMapper mapper;
    @Autowired
    private OrderService orderService;

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
            //存库
        });
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

}
