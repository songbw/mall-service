package com.fengchao.order.service;

import com.fengchao.order.bean.KuaidiCodeQueryBean;
import com.fengchao.order.bean.PageBean;
import com.fengchao.order.model.KuaidiCode;
import com.github.pagehelper.PageInfo;

/**
 * 购物车服务
 */
public interface KuaidiCodeService {

    Integer add(KuaidiCode bean) ;

    Integer delete(Integer id) ;

    Integer modify(KuaidiCode bean) ;

    PageInfo<KuaidiCode> findList(KuaidiCodeQueryBean bean) ;

}
