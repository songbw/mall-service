package com.fengchao.order.dao;

import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.*;
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

    private OrderDetailMapper orderDetailMapper;

    @Autowired
    public AdminOrderDao(OrdersMapper ordersMapper,
                         OrderDetailMapper orderDetailMapper) {
        this.ordersMapper = ordersMapper;
        this.orderDetailMapper = orderDetailMapper;
    }

    /**
     * 查询需要导出的订单主表信息
     *
     * @param orders
     * @param payStartDate
     * @param payEndDate
     * @return
     */
    public List<Orders> selectExportOrders(Orders orders, Date payStartDate, Date payEndDate) {
        OrdersExample ordersExample = new OrdersExample();
        ordersExample.setOrderByClause("id desc");

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

        return ordersList;
    }

    /**
     * 查询需要导出的订单子表信息
     *
     * @param ordersIdList
     * @param subOrderId 查询条件：子订单号
     * @param merchantId 查询条件：商家id
     * @return
     */
    public List<OrderDetail> selectExportOrderDetail(List<Integer> ordersIdList, String subOrderId, Integer merchantId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();

        criteria.andOrderIdIn(ordersIdList);

        if (StringUtils.isNotBlank(subOrderId)) {
            criteria.andSubOrderIdEqualTo(subOrderId);
        }

        if (merchantId != null && merchantId > 0) {
            criteria.andMerchantIdEqualTo(merchantId);
        }

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

}
