package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.mapper.CouponTagsMapper;
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


    @Override
    public int createTags(CouponTags bean) {
        return mapper.insertSelective(bean);
    }

    @Override
    public PageBean findTags(Integer offset, Integer limit) {
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
    public int updateTags(CouponTags bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public int deleteTags(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
