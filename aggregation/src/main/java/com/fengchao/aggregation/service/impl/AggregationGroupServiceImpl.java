package com.fengchao.aggregation.service.impl;

import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.bean.QueryBean;
import com.fengchao.aggregation.exception.AggregationException;
import com.fengchao.aggregation.mapper.AggregationGroupMapper;
import com.fengchao.aggregation.mapper.AggregationMapper;
import com.fengchao.aggregation.model.Aggregation;
import com.fengchao.aggregation.model.AggregationGroup;
import com.fengchao.aggregation.service.AggregationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AggregationGroupServiceImpl implements AggregationGroupService {

    @Autowired
    private AggregationGroupMapper mapper;
    @Autowired
    private AggregationMapper aggregationMapper;

    @Override
    public int createGroup(AggregationGroup bean) {
        return mapper.insertSelective(bean);
    }

    @Override
    public PageBean findGroup(QueryBean bean, Integer merchantId) throws AggregationException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("appId",bean.getAppId());
        if(merchantId != 0){
            map.put("merchantId",merchantId);
        }
        List<AggregationGroup> groups = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            groups = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, groups, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int updateGroup(AggregationGroup bean) throws AggregationException  {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public int deleteGroup(Integer id) throws AggregationException {
        int num = 0;
        List<Aggregation> aggregations = aggregationMapper.selectByGroupId(id);
        if(aggregations.isEmpty()){
            num = mapper.deleteByPrimaryKey(id);
        }
        return num;
    }
}
