package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.PageBean;
import com.fengchao.statistics.bean.QueryUrlBean;
import com.fengchao.statistics.bean.QueryUrlStatisBean;

import java.util.HashMap;
import java.util.List;

public interface BaiduStatisService {

    PageBean queryAllUrls(QueryUrlBean queryBean);

    HashMap<String, Object> getTotalPVandUv();

    PageBean queryStatisticsData(QueryUrlStatisBean queryBean);
}
