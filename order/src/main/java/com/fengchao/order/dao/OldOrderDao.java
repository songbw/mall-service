package com.fengchao.order.dao;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.fengchao.order.mapper.ImsMcMembersMapper;
import com.fengchao.order.mapper.ImsSupermanMallOrderItemMapper;
import com.fengchao.order.mapper.ImsSupermanMallOrderMapper;
import com.fengchao.order.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songbw
 * @date 2019/10/30 18:15
 */
@Component
@Slf4j
public class OldOrderDao {

    private ImsSupermanMallOrderMapper imsSupermanMallOrderMapper ;
    private ImsSupermanMallOrderItemMapper imsSupermanMallOrderItemMapper ;
    private ImsMcMembersMapper imsMcMembersMapper ;

    @Autowired
    public OldOrderDao(ImsSupermanMallOrderMapper imsSupermanMallOrderMapper, ImsSupermanMallOrderItemMapper imsSupermanMallOrderItemMapper, ImsMcMembersMapper imsMcMembersMapper) {
        this.imsSupermanMallOrderMapper = imsSupermanMallOrderMapper;
        this.imsSupermanMallOrderItemMapper = imsSupermanMallOrderItemMapper;
        this.imsMcMembersMapper = imsMcMembersMapper ;
    }

    /**
     * 根据商户id， 分页查询已支付的主订单列表
     *
     * @param queryBean
     * @return
     */
    public PageInfo<ImsSupermanMallOrder> selectAllPageable(OldOrderQueryBean queryBean) {
        ImsSupermanMallOrderExample imsSupermanMallOrderExample = new ImsSupermanMallOrderExample();
        ImsSupermanMallOrderExample.Criteria criteria = imsSupermanMallOrderExample.createCriteria();
        imsSupermanMallOrderExample.setOrderByClause("id desc");
        criteria.andUidEqualTo(queryBean.getUid());
        List<Byte> bytes = new ArrayList<>();
        bytes.add((byte) -3) ;
        bytes.add((byte) -2) ;
        bytes.add((byte) -1) ;
        criteria.andStatusNotIn(bytes) ;
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<ImsSupermanMallOrder> ordersList = imsSupermanMallOrderMapper.selectByExample(imsSupermanMallOrderExample);
        PageInfo<ImsSupermanMallOrder> pageInfo = new PageInfo(ordersList);
        List<ImsSupermanMallOrder> orders = new ArrayList<>() ;
        pageInfo.getList().forEach(imsSupermanMallOrder -> {
            List<ImsSupermanMallOrderItem> items = new ArrayList<>() ;
            items = selectItemByOrderId(imsSupermanMallOrder.getId()) ;
            imsSupermanMallOrder.setItemList(items);
            orders.add(imsSupermanMallOrder) ;
        });
        pageInfo.setList(orders);
        return pageInfo;
    }

    public List<ImsSupermanMallOrderItem> selectItemByOrderId(Integer orderId) {
        ImsSupermanMallOrderItemExample imsSupermanMallOrderItemExample = new ImsSupermanMallOrderItemExample() ;
        ImsSupermanMallOrderItemExample.Criteria criteria = imsSupermanMallOrderItemExample.createCriteria() ;
        criteria.andOrderidEqualTo(orderId) ;
        List<ImsSupermanMallOrderItem> items = imsSupermanMallOrderItemMapper.selectByExample(imsSupermanMallOrderItemExample) ;
        return items ;
    }

    public ImsMcMembers selectMembersByMobile(String mobile) {
        ImsMcMembersExample example = new ImsMcMembersExample() ;
        ImsMcMembersExample.Criteria criteria = example.createCriteria() ;
        criteria.andMobileEqualTo(mobile) ;
        List<ImsMcMembers> items = imsMcMembersMapper.selectByExample(example) ;
        if (items != null && items.size() > 0) {
            return items.get(0) ;
        }
        return null;
    }
}
