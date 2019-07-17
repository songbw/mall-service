package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.Promotion;
import com.fengchao.statistics.bean.PromotionPaymentBean;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.EquityService;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.mapper.PromotionOverviewMapper;
import com.fengchao.statistics.model.PromotionOverview;
import com.fengchao.statistics.service.PromotionOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromotionOverviewServiceImpl implements PromotionOverviewService {

    @Autowired
    private PromotionOverviewMapper mapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EquityService equityService;

    @Override
    public void add(QueryBean queryBean) {
        List<PromotionPaymentBean> promotionPaymentBeans = getPromotionCount(queryBean) ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = null ;
        try {
            date = formatter.parse(queryBean.getStartTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date createDate = new Date() ;
        Date finalDate = date;
        promotionPaymentBeans.forEach(promotionPaymentBean -> {
            PromotionOverview promotionOverview = new PromotionOverview();
            promotionOverview.setCreatedAt(createDate);
            promotionOverview.setStatisticsDate(finalDate);
            promotionOverview.setOrderPaymentAmount(promotionPaymentBean.getSaleAmount());
            promotionOverview.setPromotionId(promotionPaymentBean.getPromotionId());
            // 查询活动信息
            Promotion promotion = getPromotionInfo(promotionOverview.getPromotionId()) ;
            if (promotion != null) {
                promotionOverview.setPromotionName(promotion.getName());
            }
            //存库
            mapper.insertSelective(promotionOverview) ;
        });
    }

    @Override
    public List<PromotionOverview> findByDate(QueryBean queryBean) {
        HashMap map = new HashMap() ;
        map.put("start", queryBean.getStartTime());
        map.put("end", queryBean.getEndTime()) ;
        return mapper.selectByDate(map);
    }

    private List<PromotionPaymentBean> getPromotionCount(QueryBean queryBean) {
        OperaResult result = orderService.paymentPromotionCount(queryBean.getStartTime(), queryBean.getEndTime());
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionPaymentBean> promotionPaymentBeans = JSONObject.parseArray(jsonString, PromotionPaymentBean.class) ;
            return promotionPaymentBeans;
        }
        return null;
    }

    private Promotion getPromotionInfo(int id) {
        OperaResult result = equityService.findPromotion(id) ;
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            Promotion promotion = JSONObject.parseObject(jsonString, Promotion.class) ;
            return promotion;
        }
        return null;
    }
}
