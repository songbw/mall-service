package com.fengchao.order.dao;

import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.Orders;
import com.fengchao.order.model.OrdersExample;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:12
 *
 *
 */
@Component
@Slf4j
public class AdminOrderDao {

    private OrdersMapper ordersMapper;

    @Autowired
    public AdminOrderDao(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    public List<Orders> selectExportOrders(Orders orders, Date payStartDate, Date payEndDate) {
        OrdersExample ordersExample = new OrdersExample();
        ordersExample.setOrderByClause("order by id desc");

        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        if (StringUtils.isNotBlank(orders.getTradeNo())) {
            criteria.andTradeNoEqualTo(orders.getTradeNo());
        }

        if (StringUtils.isNotBlank(orders.getMobile())) {
            criteria.andMobileEqualTo(orders.getMobile());
        }

        if (orders.getStatus() != null) {
            criteria.andStatusEqualTo(orders.getStatus());
        }

        if (payStartDate != null) {
            criteria.andPaymentAtGreaterThanOrEqualTo(payStartDate);
        }

        if (payEndDate != null) {
            criteria.andPaymentAtLessThanOrEqualTo(payEndDate);
        }

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        log.info("导出订单 查询数据库结果List<Orders>:{}", JSONUtil.toJsonString(ordersList));

        return ordersList;
    }

}
