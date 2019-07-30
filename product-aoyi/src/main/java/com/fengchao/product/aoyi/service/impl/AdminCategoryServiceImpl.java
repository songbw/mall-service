package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.dao.CategoryDao;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.service.AdminCategoryService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private AoyiBaseCategoryXMapper mapper;

    @Autowired
    private CategoryDao categoryDao;

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectLimit(Integer offset, Integer limit, Integer categoryClass) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("categoryClass", categoryClass);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategoryX> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectLimit(map);
            for(AoyiBaseCategoryX categorie : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",categorie.getCategoryId());
                categorie.setSubTotal(mapper.selectLimitCount(hashMap));
            }
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectNameList(Integer offset, Integer limit,String categoryName) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("categoryName", categoryName);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategoryX> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectNameList(map);
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseCategoryX> selectCategoryList(Integer id, boolean includeSub) {
        List<AoyiBaseCategoryX> categories = mapper.selectListById(id) ;
        if(null != categories && categories.size() > 0){
            for (AoyiBaseCategoryX Category : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",Category.getCategoryId());
                Category.setSubTotal(mapper.selectLimitCount(hashMap));
                if(includeSub)
                getChildList(Category);
            }
        }
        return categories;
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseCategoryX> selectAll() {
        return mapper.selectAll();
    }

    /**
     * @Description: 递归查询
     * @param Category
     * @return void    返回类型
     * @throws
     */
    private void getChildList(AoyiBaseCategoryX Category) {
        try {
            List<AoyiBaseCategoryX> list = mapper.selectAdminListByParentId(Category.getCategoryId());
            Category.setSubs(list);
            HashMap hashMap = new HashMap();
            hashMap.put("parentId",Category.getCategoryId());
            Category.setSubTotal(mapper.selectLimitCount(hashMap));
            if(null != list && list.size() > 0){
                for (AoyiBaseCategoryX form : list) {
                    getChildList(form);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = "category")
    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean selectSubLevelList(Integer offset, Integer limit, Integer parentId) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("parentId", parentId);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategoryX> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectLimit(map);
            for(AoyiBaseCategoryX categorie : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",categorie.getCategoryId());
                categorie.setSubTotal(mapper.selectLimitCount(hashMap));
            }
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public List<CategoryQueryBean> selectByCategoryIdList(List<String> categories) {
        return mapper.selectByCategoryIdList(categories);
    }

    @CachePut(value = "category", key = "#category.categoryId")
    @Override
    public int insertSelective(AoyiBaseCategoryX bean) {
        int categoryId = mapper.selectMaxIdByParentId(bean.getParentId()) ;
        if (categoryId == 0) {
            if ("2".equals(bean.getCategoryClass())) {
                categoryId = bean.getParentId() * 100;
            } else if ("3".equals(bean.getCategoryClass())) {
                categoryId = bean.getParentId() * 10000;
            }
        }
        bean.setCategoryId(categoryId + 1);
        bean.setIdate(new Date());
        mapper.insertSelective(bean);
        return bean.getCategoryId();
    }

    @CacheEvict(value = "category", key = "#id")
    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @CachePut(value = "category", key = "#category.categoryId")
    @Override
    public void updateByPrimaryKeySelective(CategoryBean bean) {
        AoyiBaseCategoryX category = new AoyiBaseCategoryX();

        category.setCategoryId(bean.getCategoryId());
        category.setCategoryName(bean.getCategoryName());
        category.setCategoryClass(bean.getCategoryClass());
        category.setCategoryDesc(bean.getCategoryDesc());
        category.setCategoryIcon(bean.getCategoryIcon());
        category.setSortOrder(bean.getSortOrder());
        category.setParentId(bean.getParentId());
        category.setIsShow(bean.getIsShow());

        mapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public List<CategoryQueryBean> queryCategorysByCategoryIdList(List<Integer> categoryIdList) throws Exception {
        log.info("根据id集合查询品类列表 数据库入参:{}", JSONUtil.toJsonString(categoryIdList));

        // 执行查库
        List<AoyiBaseCategory> aoyiBaseCategoryList = categoryDao.selectByCategoryIdList(categoryIdList);
        log.info("根据id集合查询品类列表 数据库返回:{}", JSONUtil.toJsonString(aoyiBaseCategoryList));

        // 转dto
        List<CategoryQueryBean> categoryQueryBeanList = new ArrayList<>();
        for (AoyiBaseCategory aoyiBaseCategory : aoyiBaseCategoryList) {
            CategoryQueryBean categoryQueryBean = convertToCategoryQueryBean(aoyiBaseCategory);
            categoryQueryBeanList.add(categoryQueryBean);
        }

        log.info("根据id集合查询品类列表 AdminCategoryServiceImpl#queryCategorysByCategoryIdList 返回:{}",
                JSONUtil.toJsonString(categoryQueryBeanList));

        return categoryQueryBeanList;
    }

    // ====================================== private ===================================

    /**
     *
     * @param aoyiBaseCategory
     * @return
     */
    private CategoryQueryBean convertToCategoryQueryBean(AoyiBaseCategory aoyiBaseCategory) {
        CategoryQueryBean categoryQueryBean = new CategoryQueryBean();

        categoryQueryBean.setId(aoyiBaseCategory.getCategoryId());
        categoryQueryBean.setName(aoyiBaseCategory.getCategoryName());

        return categoryQueryBean;
    }
}
