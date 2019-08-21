package com.fengchao.order.dao;

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

    public int insert(OrderDetail record){
        return orderDetailMapper.insertSelective(record);
    }

}
