package com.fengchao.order.dao;

import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.OrderDetailExample;
import com.fengchao.order.model.Orders;
import com.fengchao.order.model.OrdersExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:12
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
     * 根据orders的id集合查询 订单列表
     *
     * @param orderIdList
     * @return
     */
    public List<Orders> selectOrdersListByIdList(List<Integer> orderIdList) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        criteria.andIdIn(orderIdList);

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
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
        criteria.andPaymentAtGreaterThanOrEqualTo(payStartDate);
        criteria.andPaymentAtLessThanOrEqualTo(payEndDate);

        if (StringUtils.isNotBlank(orders.getTradeNo())) {
            criteria.andTradeNoEqualTo(orders.getTradeNo());
        }

        if (StringUtils.isNotBlank(orders.getMobile())) {
            criteria.andMobileEqualTo(orders.getMobile());
        }

        if (orders.getStatus() != null) {
            criteria.andStatusEqualTo(orders.getStatus());
        }

        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample);

        return ordersList;
    }

    /**
     * 查询需要导出的订单子表信息
     *
     * @param ordersIdList
     * @param subOrderId   查询条件：子订单号
     * @param merchantId   查询条件：商家id
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

    /**
     * 按照创建时间范围，查询子订单集合
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public List<OrderDetail> selectOrderDetailsByCreateTimeRange(Date startDateTime, Date endDateTime) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();

        criteria.andCreatedAtBetween(startDateTime, endDateTime);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

    /**
     * 根据支付单号查询已支付列表
     * @param paymentNo
     * @return
     */
    public List<Orders> selectPaymentStatusByPaymentNo(String paymentNo) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andPaymentNoEqualTo(paymentNo) ;
        criteria.andStatusEqualTo(1) ;
        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample) ;
        return ordersList;
    }

    /**
     * 根据支付单号和openId查询订单列表
     * @param paymentNo
     * @return
     */
    public List<Orders> selectByPaymentNoAndOpenId(String paymentNo, String openId) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andPaymentNoEqualTo(paymentNo) ;
        criteria.andOpenIdEqualTo(openId) ;
        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample) ;
        return ordersList;
    }

    /**
     * 更新子订单状态
     * @param orderDetail
     */
    public void updateOrderDetailStatus(OrderDetail orderDetail) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdEqualTo(orderDetail.getOrderId()) ;
        orderDetailMapper.updateByExampleSelective(orderDetail, orderDetailExample) ;
    }

    /**
     * 更新子订单
     * @param orderDetail
     * @return
     */
    public Integer updateOrderDetail(OrderDetail orderDetail) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        OrderDetail temp = new OrderDetail() ;
        temp.setId(orderDetail.getId());
        temp.setRemark(orderDetail.getRemark());
        temp.setUpdatedAt(new Date());
        criteria.andIdEqualTo(temp.getId()) ;
        orderDetailMapper.updateByExampleSelective(temp, orderDetailExample) ;
        return orderDetail.getId() ;
    }

    /**
     * 根据ID获取主订单信息
     * @param id
     * @return
     */
    public Orders selectById(Integer id) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andIdEqualTo(id) ;
        Orders orders = ordersMapper.selectByPrimaryKey(id) ;
        return orders;
    }

    /**
     * 修改订单状态和优惠券状态
     * @param bean
     * @return
     */
    public Integer updateStatusAndCouponStatusById(Orders bean) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andIdEqualTo(bean.getId()) ;
        Orders temp = new Orders();
        temp.setUpdatedAt(bean.getUpdatedAt());
        temp.setStatus(bean.getStatus());
        if (bean.getCouponCode() != null) {
            temp.setCouponStatus(bean.getCouponStatus());
        }
        ordersMapper.updateByExampleSelective(temp, ordersExample) ;
        return bean.getId();
    }

    /**
     * 根据优惠券信息查询主订单列表
     * @param couponId
     * @param couponCode
     * @return
     */
    public List<Orders> selectByCouponIdAndCouponCode(Integer couponId, String couponCode, Integer couponStatus) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andCouponIdEqualTo(couponId) ;
        criteria.andCouponCodeEqualTo(couponCode) ;
        criteria.andCouponStatusEqualTo(couponStatus) ;
        List<Orders> ordersList = ordersMapper.selectByExample(ordersExample) ;
        return ordersList;
    }

}
