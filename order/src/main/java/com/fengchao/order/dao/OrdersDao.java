package com.fengchao.order.dao;

import com.fengchao.order.constants.PaymentStatusEnum;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.Orders;
import com.fengchao.order.model.OrdersExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-8-6 下午4:54
 */
@Component
@Slf4j
public class OrdersDao {

    private OrdersMapper ordersMapper;

    @Autowired
    public OrdersDao(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    /**
     * 根據ordersIdList 查詢 已支付的訂單
     *
     * @param ordersIdList
     * @return
     */
    public List<Orders> selectPayedOrdersListByOrdersId(List<Integer> ordersIdList) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        criteria.andPayStatusEqualTo(PaymentStatusEnum.PAY_SUCCESS.getValue());
        criteria.andIdIn(ordersIdList);

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
    }

    /**
     * 根据支付时间范围查询已支付的订单列表
     *
     * @param startPayTime yyyy-MM-dd HH:mm:ss
     * @param endPayTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public List<Orders> selectPayedOrdersListByPaymentTime(Date startPayTime, Date endPayTime) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        criteria.andPaymentAtBetween(startPayTime, endPayTime);
        criteria.andPayStatusEqualTo(PaymentStatusEnum.PAY_SUCCESS.getValue());

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
    }

    public List<Orders> selectOrderListByOpenId(String openId) {

        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        criteria.andOpenIdEqualTo(openId);

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
    }

    /**
     * 根据id集合查询
     *
     * @param idList
     * @return
     */
    public List<Orders> selectOrdersListByIdList(List<Integer> idList) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andIdIn(idList);

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
    }

    /**
     * 根据商户id， 分页查询已支付的主订单列表
     *
     * @param merchantId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageInfo<Orders> selectPayedOrderListByMerchantIdPageable(Integer merchantId, Integer pageNo, Integer pageSize) {
        OrdersExample ordersExample = new OrdersExample();

        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andMerchantIdEqualTo(merchantId);
        criteria.andPayStatusEqualTo(PaymentStatusEnum.PAY_SUCCESS.getValue());

        PageHelper.startPage(pageNo, pageSize);

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        PageInfo<Orders> pageInfo = new PageInfo(ordersList);

        return pageInfo;
    }
}
