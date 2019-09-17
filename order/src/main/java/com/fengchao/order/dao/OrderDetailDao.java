package com.fengchao.order.dao;

import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.OrderDetailExample;
import com.fengchao.order.model.Orders;
import com.fengchao.order.model.OrdersExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-8-6 下午4:16
 */
@Component
@Slf4j
public class OrderDetailDao {

    private OrderDetailMapper orderDetailMapper;
    private OrdersMapper ordersMapper;

    @Autowired
    public OrderDetailDao(OrderDetailMapper orderDetailMapper, OrdersMapper ordersMapper) {
        this.orderDetailMapper = orderDetailMapper;
        this.ordersMapper = ordersMapper;
    }

    public PageInfo<OrderDetail> selectOrderDetailsByMerchantIdPageable(Integer merchantId, Integer pageNo, Integer pageSize) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andMerchantIdEqualTo(merchantId);

        PageHelper.startPage(pageNo, pageSize);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        PageInfo<OrderDetail> pageInfo = new PageInfo(orderDetailList);

        return pageInfo;
    }

    /**
     * 根据主订单id集合查询子订单集合
     *
     * @param ordersIdList
     * @return
     */
    public List<OrderDetail> selectOrderDetailsByOrdersIdList(List<Integer> ordersIdList) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdIn(ordersIdList);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

    /**
     *
     *
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<OrderDetail> selectOrderDetailsForReconciliation(Integer merchantId, Date startTime, Date endTime) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andMerchantIdEqualTo(merchantId);


        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

    public int insert(OrderDetail record){
        return orderDetailMapper.insertSelective(record);
    }

    /**
     * 根据主订单id集合批量更新子订单状态
     *
     * @param orderIdList
     * @param status
     * @return
     */
    public int batchUpdateStatusByOrderIdList(List<Integer> orderIdList, Integer status) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdIn(orderIdList);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(status);

        int count = orderDetailMapper.updateByExampleSelective(orderDetail, orderDetailExample);
        return count;
    }

    /**
     * 更新子订单状态
     * @param orderDetail
     * @return
     */
    public Integer updateOrderDetailStatus(OrderDetail orderDetail) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        OrderDetail temp = new OrderDetail() ;
        temp.setId(orderDetail.getId());
        temp.setStatus(orderDetail.getStatus());
        temp.setUpdatedAt(new Date());
        criteria.andIdEqualTo(temp.getId()) ;
        orderDetailMapper.updateByExampleSelective(temp, orderDetailExample) ;
        if (orderDetail.getStatus() == 4 || orderDetail.getStatus() == 5) {
            OrderDetail findOrderDetail = orderDetailMapper.selectByPrimaryKey(orderDetail.getId()) ;
            //  判断主订单所属子订单是否全部为取消
            OrderDetailExample orderDetailExample1 = new OrderDetailExample() ;
            OrderDetailExample.Criteria criteria1 = orderDetailExample1.createCriteria();
            criteria1.andOrderIdEqualTo(findOrderDetail.getOrderId());
            List<Integer> list = new ArrayList<>();
            list.add(4) ;
            list.add(5) ;
            criteria1.andStatusNotIn(list) ;
            List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample1) ;
            // 更新主订单状态为取消
            if (orderDetailList == null || orderDetailList.size() == 0) {
                OrdersExample ordersExample = new OrdersExample() ;
                OrdersExample.Criteria oCriteria = ordersExample.createCriteria();
                Orders orders = new Orders();
                orders.setStatus(3);
                oCriteria.andIdEqualTo(findOrderDetail.getOrderId());
                ordersMapper.updateByExampleSelective(orders, ordersExample) ;
            }
        }
        return orderDetail.getId() ;
    }

}
