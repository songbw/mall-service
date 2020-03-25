package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;

import java.util.HashMap;
import java.util.List;

public interface AoyiBaseCategoryXMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(AoyiBaseCategoryX record);

    int insertSelective(AoyiBaseCategoryX record);

    AoyiBaseCategoryX selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(AoyiBaseCategoryX record);

    int updateByPrimaryKey(AoyiBaseCategoryX record);

    List<AoyiBaseCategoryX> selectOneLevelList();

    List<AoyiBaseCategoryX> selectListByParentId(Integer parentId);

    int selectLimitCount(HashMap map) ;

    List<AoyiBaseCategoryX> selectLimit(HashMap map) ;

    List<AoyiBaseCategoryX> selectNameList(HashMap map);

    List<AoyiBaseCategoryX> selectListById(Integer categoryId);

    List<AoyiBaseCategoryX> selectAdminListByParentId(Integer parentId);

    List<AoyiBaseCategoryX> selectAll();

    List<CategoryQueryBean> selectByCategoryIdList(List<String> list);

    int selectMaxIdByParentId(Integer parentId) ;

    /**
     * 批量插入
     *
     * @param aoyiBaseCategoryList
     */
    void batchInsert(List<AoyiBaseCategory> aoyiBaseCategoryList);
}