package com.fengchao.order.service.impl;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.RefundOrderQueryBean;
import com.fengchao.order.mapper.OrderDetailXMapper;
import com.fengchao.order.mapper.RefundOrderMapper;
import com.fengchao.order.model.OrderDetailX;
import com.fengchao.order.model.RefundOrder;
import com.fengchao.order.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    @Autowired
    private RefundOrderMapper mapper;

    @Autowired
    private OrderDetailXMapper orderDetailMapper;

    @Override
    public Integer add(RefundOrder bean) {
        // 查询是否有未完成退换货记录，如果没有允许添加，如果有则不运行添加
        RefundOrder temp = new RefundOrder();
        temp.setSubOrderId(bean.getSubOrderId());
        temp.setStatus(3);
        List<RefundOrder> refundOrders = mapper.selectBySubOrderId(temp) ;
        if (refundOrders != null && refundOrders.size() > 0) {
            // 提示不能重复提交
            return 0;
        } else {
            List<OrderDetailX> orderDetailXES = orderDetailMapper.selectBySubOrderId(bean.getSubOrderId());
            if (orderDetailXES != null && orderDetailXES.size() > 0) {
                Date date = new Date();
                bean.setCreatedAt(date);
                bean.setReturnOrderNo(date.getTime() + "");
                return mapper.insert(bean);
            }
            return 0;
        }
    }

    @Override
    public Integer update(RefundOrder bean) {
        bean.setUpdatedAt(new Date());
        bean.setStatus(0);
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public Integer delete(Integer id) {
        return null;
    }

    @Override
    public Integer updateStatus(RefundOrder bean) {
        RefundOrder refundOrder = mapper.selectByPrimaryKey(bean.getId());
        if (refundOrder != null) {
            bean.setUpdatedAt(new Date());
            return mapper.updateStatusById(bean);
        }
        return 0;
    }

    @Override
    public PageBean findList(RefundOrderQueryBean queryBean) {
        return null;
    }

    @Override
    public List<RefundOrder> findBySubOrderId(String subOrderId) {
        RefundOrder temp = new RefundOrder();
        temp.setSubOrderId(subOrderId);
        temp.setStatus(null);
        List<RefundOrder> refundOrders = mapper.selectBySubOrderId(temp) ;
        return refundOrders;
    }
}
