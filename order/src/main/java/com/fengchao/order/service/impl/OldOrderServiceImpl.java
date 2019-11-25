package com.fengchao.order.service.impl;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.dao.OldOrderDao;
import com.fengchao.order.model.ImsMcMembers;
import com.fengchao.order.service.OldOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author songbw
 * @date 2019/10/30 18:15
 */
@Service
public class OldOrderServiceImpl implements OldOrderService {

    @Autowired
    private OldOrderDao oldOrderDao ;

    @Override
    public OperaResponse findList(OldOrderQueryBean queryBean) {
        OperaResponse response = new OperaResponse() ;
        ImsMcMembers imsMcMembers = oldOrderDao.selectMembersByMobile(queryBean.getMobile()) ;
        if (imsMcMembers == null || imsMcMembers.getUid() == null) {
            response.setCode(400001);
            response.setMsg("用户不存在");
            return response ;
        }
        queryBean.setUid(imsMcMembers.getUid());
        response.setData(oldOrderDao.selectAllPageable(queryBean));
        return response;
    }
}
