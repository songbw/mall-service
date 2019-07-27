package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.vo.CategoryOverviewResVo;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.CategoryOverviewDao;
import com.fengchao.statistics.model.CategoryOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.ProductRpcService;
import com.fengchao.statistics.rpc.extmodel.CategoryQueryBean;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.service.CategoryOverviewService;
import com.fengchao.statistics.utils.DateUtil;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryOverviewServiceImpl implements CategoryOverviewService {

    @Autowired
    private ProductRpcService productRpcService;

    @Autowired
    private OrdersRpcService ordersRpcService;

    @Autowired
    private CategoryOverviewDao categoryOverviewDao;

    @Override
    public void doDailyStatistic(List<OrderDetailBean> orderDetailBeanList, String startDateTime, String endDateTime) throws Exception {
        // 1. 根据一级品类维度将订单详情分类
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

        // 2. 获取一级品类名称
        Set<Integer> firstCategoryIdSet = orderDetailBeanListMap.keySet();
        List<CategoryQueryBean> categoryQueryBeanList =
                productRpcService.queryCategorysByCategoryIdList(new ArrayList<>(firstCategoryIdSet));
        // 转map key: categoryId  value: CategoryQueryBean
        Map<Integer, CategoryQueryBean> categoryQueryBeanMap =
                categoryQueryBeanList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

        // 3. 获取统计数据
        String statisticsDateTime =
                DateUtil.calcDay(startDateTime, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD); // 统计时间
        List<CategoryOverview> categoryOverviewList = new ArrayList<>(); // 统计数据集合
        for (Integer categoryId : firstCategoryIdSet) { // 遍历 orderDetailBeanListMap
            // 获取订单详情集合
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeanListMap.get(categoryId);
            // 计算总金额
            BigDecimal totalPrice = new BigDecimal(0);
            for (OrderDetailBean orderDetailBean : _orderDetailBeanList) {
                BigDecimal _tmpPrice = new BigDecimal(orderDetailBean.getSaleAmount());
                totalPrice = totalPrice.add(_tmpPrice);
            }


            CategoryOverview categoryOverview = new CategoryOverview();
            categoryOverview.setCategoryFcode(String.valueOf(categoryId)); // 一级品类code
            categoryOverview.setCategoryFname(categoryQueryBeanMap.get(categoryId) == null ?
                    "" : categoryQueryBeanMap.get(categoryId).getName()); // 品类名称
            categoryOverview.setOrderAmount(totalPrice.multiply(new BigDecimal(100)).longValue());
            categoryOverview.setStatisticsDate(DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));
            categoryOverview.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            categoryOverview.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            categoryOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());

            //
            categoryOverviewList.add(categoryOverview);
        }
        log.info("按照品类统计订单详情总金额数据; 统计时间范围：{} - {} 统计结果:{}",
                startDateTime, endDateTime, JSONUtil.toJsonString(categoryOverviewList));

        // 4. 插入统计数据
        // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
        categoryOverviewDao.deleteCategoryOverviewByPeriodTypeAndStatisticDate(StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));

        // 4.2 执行插入
        for (CategoryOverview categoryOverview : categoryOverviewList) {
            categoryOverviewDao.insertCategoryOverview(categoryOverview);
        }

        log.info("按照品类统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
    }

    @Override
    public List<CategoryOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate, DateUtil.DATE_YYYY_MM_DD);
        Date _endDate = DateUtil.parseDateTime(endDate, DateUtil.DATE_YYYY_MM_DD);
        log.info("根据时间范围获取daily型的品类维度统计数据 日期范围: {} - {}", _startDate, _endDate);
        List<CategoryOverview> categoryOverviewList =
                categoryOverviewDao.selectDailyCategoryOverviewsByDateRange(_startDate, _endDate);
        log.info("根据时间范围获取daily型的品类维度统计数据 数据库返回: {}", JSONUtil.toJsonString(categoryOverviewList));

        // 2. 将获取到的数据按照一级品类分组
        Map<String, List<CategoryOverview>> categoryOverviewListMap = new HashMap<>();
        for (CategoryOverview categoryOverview : categoryOverviewList) {
            String fCategoryCode = categoryOverview.getCategoryFcode(); // 一级品类code

            List<CategoryOverview> _categoryOverviewList = categoryOverviewListMap.get(fCategoryCode);
            if (_categoryOverviewList == null) {
                _categoryOverviewList = new ArrayList<>();
                categoryOverviewListMap.put(fCategoryCode, _categoryOverviewList);
            }

            _categoryOverviewList.add(categoryOverview);
        }

        // 3. 组装统计数据 CategoryOverviewResVo
        List<CategoryOverviewResVo> categoryOverviewResVoList = new ArrayList<>();
        Set<String> keySet = categoryOverviewListMap.keySet();
        for (String key : keySet) {
            List<CategoryOverview> _categoryOverviewList = categoryOverviewListMap.get(key);

            String categoryName = ""; // 一级品类名称
            Long totalAmount = 0L; // 单位：分
            for (CategoryOverview categoryOverview : _categoryOverviewList) {
                totalAmount = totalAmount + categoryOverview.getOrderAmount();
                categoryName = categoryOverview.getCategoryFname();
            }

            CategoryOverviewResVo categoryOverviewResVo = new CategoryOverviewResVo();
            categoryOverviewResVo.setCategoryId(Integer.valueOf(key));
            categoryOverviewResVo.setCategoryName(categoryName);
            categoryOverviewResVo.setTotalAmount(new BigDecimal(totalAmount).divide(new BigDecimal(100)).toString());

            categoryOverviewResVoList.add(categoryOverviewResVo);
        }

        log.info("根据时间范围获取daily型的品类维度统计数据 获取统计数据List<CategoryOverviewResVo>:{}",
                JSONUtil.toJsonString(categoryOverviewResVoList));


        return categoryOverviewResVoList;
    }

    // ========================================= private ===================

}
