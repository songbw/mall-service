package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.*;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.CategoryOverviewDao;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.feign.ProductServiceClient;
import com.fengchao.statistics.model.CategoryOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.service.StatisticService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryOverviewServiceImpl implements StatisticService {

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductServiceClient productService;

    @Autowired
    private OrdersRpcService ordersRpcService;

    @Autowired
    private CategoryOverviewDao categoryOverviewDao;

    @Override
    public void doStatistic(String startDateTime, String endDateTime) {
        // 1. 根据时间范围查询已支付的订单详情
        List<OrderDetailBean> orderDetailBeanList = ordersRpcService.queryPayedOrderDetailList(startDateTime, endDateTime);

        // 2. 根据一级品类维度将订单详情分类
        Map<Integer, List<OrderDetailBean>> orderDetailBeanListMap = new HashMap<>();
        for (OrderDetailBean orderDetailBean : orderDetailBeanList) { // 遍历订单详情
            // 处理品类信息
            String category = orderDetailBean.getCategory();
            if (StringUtils.isNotBlank(category)) {
                // 一级品类信息
                Integer firstCategory = Integer.valueOf(category.substring(0, 2));

                List<OrderDetailBean> _list = orderDetailBeanListMap.get(firstCategory);
                if (_list == null) {
                    _list = new ArrayList<>();
                    orderDetailBeanListMap.put(firstCategory, _list);
                }
                _list.add(orderDetailBean);
            }
        }

        // 3. 获取一级品类名称


        // 4. 执行统计
        List<CategoryOverview> categoryOverviewList = new ArrayList<>();

        // 遍历
        Set<Integer> keySet = orderDetailBeanListMap.keySet();
        for (Integer key : keySet) {
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeanListMap.get(key);

            CategoryOverview categoryOverview = new CategoryOverview();
            categoryOverview.setCategoryFcode(String.valueOf(key)); // 一级品类code
            categoryOverview.setCategoryFname(); //
            categoryOverview.setOrderAmount();
            categoryOverview.setStatisticsDate();
            categoryOverview.setStatisticStartTime();
            categoryOverview.setStatisticEndTime();
            categoryOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
        }



//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
//        Date date = null ;
//        try {
//            date = formatter.parse(queryBean.getStartTime()) ;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date createDate = new Date() ;
//        Date finalDate = date;
//        categoryPaymentBeans.forEach(categoryPaymentBean -> {
//            CategoryOverview categoryOverview = new CategoryOverview();
//            categoryOverview.setCreatedAt(createDate);
//            categoryOverview.setStatisticsDate(finalDate);
//            // 计算一级类目支付之和
//            categoryOverview.setOrderPaymentAmount(categoryPaymentBean.getSaleAmount());
////            // 查询类别信息
////            SkuCode skuCode = getMerchantInfo(merchantPaymentBean.getMerchantId()) ;
//
//            //存库
//            mapper.insertSelective(categoryOverview) ;
//        });

        CategoryOverview categoryOverview = new CategoryOverview();
        categoryOverview.setCategoryFcode(); // 一级品类code
        categoryOverview.setCategoryFname(); //
        categoryOverview.setOrderAmount();
        categoryOverview.setStatisticsDate();
        categoryOverview.setStatisticStartTime();
        categoryOverview.setStatisticEndTime();
        categoryOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());

        categoryOverviewDao.insertCategoryOverview(categoryOverview);

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
