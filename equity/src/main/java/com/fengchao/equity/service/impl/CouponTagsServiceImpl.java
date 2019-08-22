package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.CouponTagBean;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.mapper.CouponTagsMapper;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.CouponTags;
import com.fengchao.equity.service.CouponTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CouponTagsServiceImpl implements CouponTagsService {

    @Autowired
    private CouponTagsMapper mapper;
    @Autowired
    private CouponXMapper couponXMapper;


    @Override
    public int createTags(CouponTags bean) throws EquityException {
        return mapper.insertSelective(bean);
    }

    @Override
    public PageBean findTags(Integer offset, Integer limit) throws EquityException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<CouponTags> tags = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            tags = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, tags, total, offset, limit);
        return pageBean;
    }

    @Override
    public int updateTags(CouponTags bean) throws EquityException {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public CouponTagBean deleteTags(Integer id) throws EquityException {
        CouponTagBean couponTagBean = new CouponTagBean();
        List<Integer> couponIds = couponXMapper.selectActiveTagsCoupon();
        if(couponIds.isEmpty()){
            couponTagBean.setNum(mapper.deleteByPrimaryKey(id));
            return couponTagBean;
        }else{
            couponTagBean.setNum(2);
            couponTagBean.setCouponIds(couponIds);
            return couponTagBean;
        }
    }
}
