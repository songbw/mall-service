package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.CategoryOverview;

import java.util.List;

public interface CategoryOverviewService {

    void add(QueryBean queryBean) ;

    List<CategoryOverview> findsum(QueryBean queryBean) ;
}
