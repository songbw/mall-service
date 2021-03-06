package com.fengchao.order.dao;

import com.fengchao.order.bean.Logisticsbean;
import com.fengchao.order.constants.OrderDetailStatusEnum;
import com.fengchao.order.mapper.KuaidiCodeMapper;
import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private KuaidiCodeMapper kuaidiCodeMapper;

    @Autowired
    public OrderDetailDao(OrderDetailMapper orderDetailMapper, OrdersMapper ordersMapper, KuaidiCodeMapper kuaidiCodeMapper) {
        this.orderDetailMapper = orderDetailMapper;
        this.ordersMapper = ordersMapper;
        this.kuaidiCodeMapper = kuaidiCodeMapper;
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
     * 查询需要对账(入账)的订单
     *
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<OrderDetail> selectOrderDetailsForReconciliation(Integer merchantId, Date startTime, Date endTime) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();

        if (merchantId != null) {
            criteria.andMerchantIdEqualTo(merchantId);
        }

        criteria.andCompleteTimeBetween(startTime, endTime);

        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderDetailStatusEnum.COMPLETED.getValue());
        statusList.add(OrderDetailStatusEnum.APPLY_REFUND.getValue());

        criteria.andStatusIn(statusList);


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
        OrderDetail checkCompleteTime = orderDetailMapper.selectByPrimaryKey(orderDetail.getId()) ;
        OrderDetail temp = new OrderDetail() ;
        temp.setId(orderDetail.getId());
        temp.setStatus(orderDetail.getStatus());
        Date date = new Date() ;
        temp.setUpdatedAt(date);
        if (orderDetail.getStatus() == 3 || orderDetail.getStatus() == 5) {
            if (checkCompleteTime != null && checkCompleteTime.getCompleteTime() != null && checkCompleteTime.getCompleteTime().getTime() == -28800000) {
                temp.setCompleteTime(date);
            }
        }
        criteria.andIdEqualTo(temp.getId()) ;
        orderDetailMapper.updateByExampleSelective(temp, orderDetailExample) ;
        if (orderDetail.getStatus() == 3 || orderDetail.getStatus() == 4 || orderDetail.getStatus() == 5) {
            OrderDetail findOrderDetail = orderDetailMapper.selectByPrimaryKey(orderDetail.getId()) ;
            Orders orders = ordersMapper.selectByPrimaryKey(findOrderDetail.getOrderId()) ;
            if (orders != null && orders.getStatus() < 2) {
                //  判断主订单所属子订单是否全部为取消
                OrderDetailExample orderDetailExample1 = new OrderDetailExample() ;
                OrderDetailExample.Criteria criteria1 = orderDetailExample1.createCriteria();
                criteria1.andOrderIdEqualTo(findOrderDetail.getOrderId());
                List<Integer> list = new ArrayList<>();
                list.add(3) ; // 已完成
                list.add(4) ; // 已取消
                list.add(5) ; // 已取消，并申请售后
                criteria1.andStatusNotIn(list) ;
                List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample1) ;
                // 更新主订单状态为取消
                if (orderDetailList == null || orderDetailList.size() == 0) {
                    Orders ordersU = new Orders();
                    ordersU.setId(findOrderDetail.getOrderId());
                    if (orderDetail.getStatus() == 3) {
                        ordersU.setStatus(2);
                    } else {
                        ordersU.setStatus(3);
                    }
                    ordersMapper.updateByPrimaryKeySelective(ordersU) ;
                }
            }
        }
        return orderDetail.getId() ;
    }

    /**
     * 根据openId、mpu、promotionId查询子订单列表
     * @param openId
     * @param mpu
     * @param promotionId
     * @return
     */
    public List<OrderDetail> selectOrderDetailsByOpenIdAndMpuAndPromotionId(String openId, String mpu, int promotionId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andMpuEqualTo(mpu);
        criteria.andPromotionIdEqualTo(promotionId) ;
        criteria.andSubOrderIdLike("%" + openId + "%") ;
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

    /**
     * 根据子订单号更新物流信息
     * @param logisticsbean
     * @return
     */
    public int updateBySubOrderId(Logisticsbean logisticsbean) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        KuaidiCodeExample kuaidiCodeExample = new KuaidiCodeExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        KuaidiCodeExample.Criteria kuaidiCriteria = kuaidiCodeExample.createCriteria();
        OrderDetail temp = new OrderDetail() ;
        temp.setLogisticsId(logisticsbean.getLogisticsId());
        temp.setLogisticsContent(logisticsbean.getLogisticsContent());
        temp.setStatus(2);
        temp.setUpdatedAt(new Date());

        kuaidiCriteria.andNameEqualTo(logisticsbean.getLogisticsContent()) ;

        List<KuaidiCode> kuaidiCodes = kuaidiCodeMapper.selectByExample(kuaidiCodeExample);
        if (kuaidiCodes != null && kuaidiCodes.size() > 0) {
            temp.setComcode(kuaidiCodes.get(0).getCode());
        }
        criteria.andSubOrderIdEqualTo(logisticsbean.getSubOrderId()) ;
        return orderDetailMapper.updateByExampleSelective(temp, orderDetailExample) ;
    }

    /**
     * 根据子订单号查询子订单信息
     * @param subOrderId
     * @return
     */
    public OrderDetail selectBySubOrderId(String subOrderId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andSubOrderIdEqualTo(subOrderId) ;
        List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(orderDetailExample) ;
        if (orderDetails != null && orderDetails.size() > 0) {
            return orderDetails.get(0);
        }
        return null;
    }

    /**
     * 根据子订单No集合查询
     *
     * @param subOrderIdList
     * @return
     */
    public List<OrderDetail> selectOrderDetailListBySubOrderIds(List<String> subOrderIdList) {
        OrderDetailExample orderDetailExample = new OrderDetailExample() ;
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();

        criteria.andSubOrderIdIn(subOrderIdList);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);
        return orderDetailList;
    }

    /**
     * 根据主订单id查询子订单集合
     *
     * @param ordersId
     * @return
     */
    public List<OrderDetail> selectOrderDetailsByOrdersId(Integer ordersId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();

        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdEqualTo(ordersId);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(orderDetailExample);

        return orderDetailList;
    }

}
