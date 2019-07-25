package com.fengchao.statistics.dao;

import com.fengchao.statistics.mapper.CategoryOverviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author tom
 * @Date 19-7-25 下午5:17
 */
@Component
public class CategoryOverviewDao {

    private CategoryOverviewMapper categoryOverviewMapper;

    @Autowired
    public CategoryOverviewDao(CategoryOverviewMapper categoryOverviewMapper) {
        this.categoryOverviewMapper = categoryOverviewMapper;
    }
}
