package com.fengchao.order.service.impl;

import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.mapper.ShoppingCartMapper;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper mapper;

    @Override
    public Integer add(ShoppingCart bean) {
        ShoppingCart temp = mapper.selectByOpenIdAndSkuId(bean) ;
        Date date = new Date() ;
        if (temp == null) {
            bean.setCreatedAt(date);
            bean.setUpdatedAt(date);
            bean.setCount(1);
            mapper.insert(bean) ;
            return bean.getId() ;
        } else {
            temp.setCount(temp.getCount() + 1);
            temp.setUpdatedAt(date);
            mapper.updateNumById(temp);
            return temp.getId();
        }
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.updateIsDelById(id);
    }

    @Override
    public Integer modifyNum(ShoppingCart bean) {
        bean.setUpdatedAt(new Date());
        return mapper.updateNumById(bean);
    }

    @Override
    public PageBean findList(ShoppingCartQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("openId", queryBean.getOpenId()) ;
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            shoppingCarts = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, shoppingCarts, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }
}
