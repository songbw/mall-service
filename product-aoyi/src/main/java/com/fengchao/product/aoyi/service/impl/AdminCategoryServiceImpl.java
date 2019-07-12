package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.service.AdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private AoyiBaseCategoryMapper mapper;

    @Override
    public PageBean selectLimit(Integer offset, Integer limit, Integer categoryClass) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("categoryClass", categoryClass);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategory> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectLimit(map);
            for(AoyiBaseCategory categorie : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",categorie.getCategoryId());
                categorie.setSubTotal(mapper.selectLimitCount(hashMap));
            }
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @Override
    public PageBean selectNameList(Integer offset, Integer limit,String categoryName) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("categoryName", categoryName);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategory> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectNameList(map);
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @Override
    public List<AoyiBaseCategory> selectCategoryList(Integer id, boolean includeSub) {
        List<AoyiBaseCategory> categories = mapper.selectListById(id) ;
        if(null != categories && categories.size() > 0){
            for (AoyiBaseCategory Category : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",Category.getCategoryId());
                Category.setSubTotal(mapper.selectLimitCount(hashMap));
                if(includeSub)
                getChildList(Category);
            }
        }
        return categories;
    }

    /**
     * @Description: 递归查询
     * @param Category
     * @return void    返回类型
     * @throws
     */
    private void getChildList(AoyiBaseCategory Category) {
        try {
            List<AoyiBaseCategory> list = mapper.selectAdminListByParentId(Category.getCategoryId());
            Category.setSubs(list);
            HashMap hashMap = new HashMap();
            hashMap.put("parentId",Category.getCategoryId());
            Category.setSubTotal(mapper.selectLimitCount(hashMap));
            if(null != list && list.size() > 0){
                for (AoyiBaseCategory form : list) {
                    getChildList(form);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageBean selectSubLevelList(Integer offset, Integer limit, Integer parentId) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("parentId", parentId);
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        List<AoyiBaseCategory> categories = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            categories = mapper.selectLimit(map);
            for(AoyiBaseCategory categorie : categories) {
                HashMap hashMap = new HashMap();
                hashMap.put("parentId",categorie.getCategoryId());
                categorie.setSubTotal(mapper.selectLimitCount(hashMap));
            }
        }
        pageBean = PageBean.build(pageBean, categories, total, offset, limit);
        return pageBean;
    }

    @Override
    public List<CategoryQueryBean> selectByCategoryIdList(List<String> categories) {
        return mapper.selectByCategoryIdList(categories);
    }

    @Override
    public int insertSelective(CategoryBean bean) {
        int categoryId = mapper.selectMaxIdByParentId(bean.getParentId()) ;
        AoyiBaseCategory category = new AoyiBaseCategory();
        category.setCategoryId(categoryId + 1);
        category.setCategoryName(bean.getCategoryName());
        category.setCategoryClass(bean.getCategoryClass());
        category.setCategoryDesc(bean.getCategoryDesc());
        category.setCategoryIcon(bean.getCategoryIcon());
        category.setSortOrder(bean.getSortOrder());
        category.setParentId(bean.getParentId());
        category.setIdate(new Date());
        mapper.insertSelective(category);
        return category.getCategoryId();
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(CategoryBean bean) {
        AoyiBaseCategory category = new AoyiBaseCategory();

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
}
