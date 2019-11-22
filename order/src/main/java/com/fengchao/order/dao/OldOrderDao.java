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
        ImsMcMembers imsMcMembers = selectMembersByMobile(queryBean.getMobile()) ;
        List<ImsSupermanMallOrder> ordersList = new ArrayList<>() ;
        if (imsMcMembers != null && imsMcMembers.getUid() != null) {
            ImsSupermanMallOrderExample imsSupermanMallOrderExample = new ImsSupermanMallOrderExample();
            ImsSupermanMallOrderExample.Criteria criteria = imsSupermanMallOrderExample.createCriteria();
            criteria.andUidEqualTo(imsMcMembers.getUid());
            PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
            imsSupermanMallOrderMapper.selectByExample(imsSupermanMallOrderExample).forEach(imsSupermanMallOrder -> {
                List<ImsSupermanMallOrderItem> items = new ArrayList<>() ;
                items = selectItemByOrderId(imsSupermanMallOrder.getId()) ;
                imsSupermanMallOrder.setItemList(items);
                ordersList.add(imsSupermanMallOrder) ;
            });
        }
        PageInfo<ImsSupermanMallOrder> pageInfo = new PageInfo(ordersList);
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
