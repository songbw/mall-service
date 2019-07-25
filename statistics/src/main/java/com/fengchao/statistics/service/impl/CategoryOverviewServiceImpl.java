package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.*;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.feign.ProductService;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CategoryOverviewServiceImpl implements StatisticService {

    @Autowired
    private CategoryOverviewMapper mapper;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductService productService;

    private OrdersRpcService ordersRpcService;

    @Override
    public void doStatistic(String startDateTime, String endDateTime) {
        List<CategoryPaymentBean> categoryPaymentBeans = getCategoryList(queryBean);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = null ;
        try {
            date = formatter.parse(queryBean.getStartTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date createDate = new Date() ;
        Date finalDate = date;
        categoryPaymentBeans.forEach(categoryPaymentBean -> {
            CategoryOverview categoryOverview = new CategoryOverview();
            categoryOverview.setCreatedAt(createDate);
            categoryOverview.setStatisticsDate(finalDate);
            // 计算一级类目支付之和
            categoryOverview.setOrderPaymentAmount(categoryPaymentBean.getSaleAmount());
//            // 查询类别信息
//            SkuCode skuCode = getMerchantInfo(merchantPaymentBean.getMerchantId()) ;

            //存库
            mapper.insertSelective(categoryOverview) ;
        });
    }

//    @Override
//    public List<CategoryOverview> findsum(QueryBean queryBean) {
//        HashMap map = new HashMap() ;
//        map.put("start", queryBean.getStartTime());
//        map.put("end", queryBean.getEndTime()) ;
//        return mapper.selectSum(map);
//    }

    private List<CategoryPaymentBean> getCategoryList(QueryBean queryBean) {
        OperaResult result = orderServiceClient.paymentCategoryList(queryBean.getStartTime(), queryBean.getEndTime());
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<CategoryPaymentBean> categoryPaymentBeans = JSONObject.parseArray(jsonString, CategoryPaymentBean.class) ;
            return categoryPaymentBeans;
        }
        return null;
    }

//    private SkuCode getMerchantInfo(int id) {
//        OperaResult result = productService.findMerchant(id) ;
//        if (result.getCode() == 200) {
//            Map<String, Object> data = result.getData() ;
//            Object object = data.get("result");
//            String jsonString = JSON.toJSONString(object);
//            SkuCode skuCode = JSONObject.parseObject(jsonString, SkuCode.class) ;
//            return skuCode;
//        }
//        return null;
//    }
}
