package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-19 下午5:41
 */
@Component
public class CategoryDao {

    private AoyiBaseCategoryMapper aoyiBaseCategoryMapper;

    @Autowired
    public CategoryDao(AoyiBaseCategoryMapper aoyiBaseCategoryMapper) {
        this.aoyiBaseCategoryMapper = aoyiBaseCategoryMapper;
    }

    /**
     * 根据categoryid集合查询
     *
     * @param categoryIdList
     * @return
     */
    public List<AoyiBaseCategory> selectByCategoryIdList(List<Integer> categoryIdList) {
        AoyiBaseCategoryExample aoyiBaseCategoryExample = new AoyiBaseCategoryExample();

        AoyiBaseCategoryExample.Criteria criteria = aoyiBaseCategoryExample.createCriteria();
        criteria.andCategoryIdIn(categoryIdList);

        List<AoyiBaseCategory> aoyiBaseCategoryList = aoyiBaseCategoryMapper.selectByExample(aoyiBaseCategoryExample);

        return aoyiBaseCategoryList;
    }

    /**
     * 根据品类级别查询品类集合
     *
     * @param categoryClass
     * @return
     */
    public List<AoyiBaseCategory> selectByCategoryClass(String categoryClass) {
        AoyiBaseCategoryExample aoyiBaseCategoryExample = new AoyiBaseCategoryExample();

        AoyiBaseCategoryExample.Criteria criteria = aoyiBaseCategoryExample.createCriteria();
        criteria.andCategoryClassEqualTo(categoryClass);

        List<AoyiBaseCategory> aoyiBaseCategoryList = aoyiBaseCategoryMapper.selectByExample(aoyiBaseCategoryExample);

        return aoyiBaseCategoryList;
    }

    /**
     * 根据品类ID列表查询品类集合
     *
     * @param categoryIds
     * @return
     */
    public List<AoyiBaseCategory> selectByCategoryIds(List<Integer> categoryIds) {
        AoyiBaseCategoryExample aoyiBaseCategoryExample = new AoyiBaseCategoryExample();
        AoyiBaseCategoryExample.Criteria criteria = aoyiBaseCategoryExample.createCriteria();
        criteria.andCategoryIdIn(categoryIds);
        List<AoyiBaseCategory> aoyiBaseCategoryList = aoyiBaseCategoryMapper.selectByExample(aoyiBaseCategoryExample);
        return aoyiBaseCategoryList;
    }

    /**
     * 添加或更新类目信息
     *
     * @param bean
     * @return
     */
    public Integer insertOrUpdate(AoyiBaseCategory bean) {
        AoyiBaseCategory aoyiBaseCategory = aoyiBaseCategoryMapper.selectByPrimaryKey(bean.getCategoryId()) ;
        if (aoyiBaseCategory != null) {
            aoyiBaseCategoryMapper.updateByPrimaryKey(bean) ;
        } else {
            bean.setIdate(new Date());
            aoyiBaseCategoryMapper.insertSelective(bean) ;
        }
        return bean.getCategoryId();
    }
}
