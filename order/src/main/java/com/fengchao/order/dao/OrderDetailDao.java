package com.fengchao.order.dao;

import com.fengchao.order.constants.OrderDetailStatusEnum;
import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.OrderDetailExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-8-6 下午4:16
 */
@Component
@Slf4j
public class OrderDetailDao {

    private OrderDetailMapper orderDetailMapper;

    @Autowired
    public OrderDetailDao(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
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

}
