package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.PageBean;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.bean.QueryUrlStatisBean;
import com.fengchao.statistics.db.annotation.DataSource;
import com.fengchao.statistics.db.config.DataSourceNames;
import com.fengchao.statistics.mapper.BaiduStatisMapper;
import com.fengchao.statistics.service.BaiduStatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BaiduStatisServiceImpl implements BaiduStatisService {

    @Autowired
    private BaiduStatisMapper mapper;

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean queryAllUrls(QueryBean queryBean) {
        HashMap params = new HashMap<>();
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        params.put("pageNo", offset);
        params.put("pageSize", queryBean.getPageSize());
        List<HashMap<String, Object>> result = mapper.queryAllUrls(params);
        Integer total = mapper.countAllUrls();
        return PageBean.build(new PageBean(), result, total, queryBean.getPageNo(), queryBean.getPageSize());
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public HashMap<String, Object> getTotalPVandUv() {
        return mapper.getTotalPVandUv();
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean queryStatisticsData(QueryUrlStatisBean queryBean) {
        HashMap params = new HashMap<>();
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        params.put("pageNo", offset);
        params.put("pageSize", queryBean.getPageSize());
        params.put("dayStart", queryBean.getStartTime());
        params.put("dayEnd", queryBean.getEndTime());
        params.put("urlKey", queryBean.getUrlKey());

        List<HashMap<String, Object>> result = mapper.queryStatisticsData(params);
        Integer total = mapper.countStatisticsData(params);
        System.out.println("total====>" + total);
        return PageBean.build(new PageBean(), result, total, queryBean.getPageNo(), queryBean.getPageSize());
    }



}