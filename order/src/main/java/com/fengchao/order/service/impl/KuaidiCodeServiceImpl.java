package com.fengchao.order.service.impl;

import com.fengchao.order.bean.KuaidiCodeQueryBean;
import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.mapper.KuaidiCodeMapper;
import com.fengchao.order.mapper.ShoppingCartMapper;
import com.fengchao.order.model.KuaidiCode;
import com.fengchao.order.model.KuaidiCodeExample;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.KuaidiCodeService;
import com.fengchao.order.service.ShoppingCartService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class KuaidiCodeServiceImpl implements KuaidiCodeService {

    @Autowired
    private KuaidiCodeMapper mapper;

    @Override
    public Integer add(KuaidiCode bean) {
        Date date = new Date() ;
        bean.setCreatedAt(date);
        mapper.insert(bean) ;
        return bean.getId() ;
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer modify(KuaidiCode bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public PageInfo<KuaidiCode> findList(KuaidiCodeQueryBean queryBean) {
        KuaidiCodeExample example = new KuaidiCodeExample();
        example.createCriteria().andNameLike("%" + queryBean.getName() + "%") ;
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<KuaidiCode> types =  mapper.selectByExample(example);
        return new PageInfo<>(types);
    }
}
