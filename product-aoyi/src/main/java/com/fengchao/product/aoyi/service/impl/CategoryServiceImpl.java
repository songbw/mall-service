package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.dao.CategoryDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.service.CategoryService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AoyiBaseCategoryXMapper mapper;

    @Autowired
    private CategoryDao categoryDao;

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseCategoryX> findOneLevelList() {
        return mapper.selectOneLevelList();
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseCategoryX> findTwoLevelListByOneLevelId(int id) {
        return mapper.selectListByParentId(id) ;
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseCategoryX> findListById(int id) {
        List<AoyiBaseCategoryX> categories = mapper.selectListByParentId(id) ;
        categories.forEach(element -> {
            element.setSubs(mapper.selectListByParentId(element.getCategoryId()));
        });
        return categories;
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<CategoryBean> queryCategoryListByCategoryIdList(List<Integer> categoryIdList) {
        log.info("根据categoryid集合查询分类信息 数据库入参:{}", JSONUtil.toJsonString(categoryIdList));
        List<AoyiBaseCategory> aoyiBaseCategoryList = categoryDao.selectByCategoryIdList(categoryIdList);
        log.info("根据categoryid集合查询分类信息 数据库返回:{}", JSONUtil.toJsonString(aoyiBaseCategoryList));

        // 转dto
        List<CategoryBean> categoryBeanList = new ArrayList<>();
        for (AoyiBaseCategory aoyiBaseCategory : aoyiBaseCategoryList) {
            CategoryBean categoryBean = convertCategoryBean(aoyiBaseCategory);

            categoryBeanList.add(categoryBean);
        }

        log.info("根据categoryid集合查询分类信息 获取List<CategoryBean>:{}", JSONUtil.toJsonString(categoryBeanList));

        return categoryBeanList;
    }

    //============================== private =======================

    /**
     *
     * @param aoyiBaseCategory
     * @return
     */
    private CategoryBean convertCategoryBean(AoyiBaseCategory aoyiBaseCategory) {
        CategoryBean categoryBean = new CategoryBean();

        categoryBean.setCategoryId(aoyiBaseCategory.getCategoryId());
        categoryBean.setCategoryName(aoyiBaseCategory.getCategoryName());
        categoryBean.setParentId(aoyiBaseCategory.getParentId());
        categoryBean.setCategoryClass(aoyiBaseCategory.getCategoryClass());
        categoryBean.setCategoryIcon(aoyiBaseCategory.getCategoryIcon());
        categoryBean.setCategoryDesc(aoyiBaseCategory.getCategoryDesc());
        categoryBean.setSortOrder(aoyiBaseCategory.getSortOrder());
        categoryBean.setIsShow(aoyiBaseCategory.getIsShow());

        return categoryBean;
    }
}
