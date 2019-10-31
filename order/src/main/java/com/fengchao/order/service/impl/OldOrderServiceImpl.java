package com.fengchao.order.service.impl;

import com.fengchao.order.bean.OldOrderQueryBean;
import com.fengchao.order.dao.OldOrderDao;
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
    public PageInfo findList(OldOrderQueryBean queryBean) {
        return oldOrderDao.selectAllPageable(queryBean);
    }
}
