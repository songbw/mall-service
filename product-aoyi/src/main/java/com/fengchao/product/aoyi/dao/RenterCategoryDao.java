package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.mapper.RenterCategoryMapper;
import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.model.RenterCategoryExample;
import com.fengchao.product.aoyi.model.StarDetailImgExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class RenterCategoryDao {

    private RenterCategoryMapper mapper;
    private AoyiBaseCategoryXMapper mapperX ;

    @Autowired
    public RenterCategoryDao(RenterCategoryMapper mapper, AoyiBaseCategoryXMapper mapperX ) {
        this.mapper = mapper;
        this.mapperX = mapperX ;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<CategoryBean> selectByRenterId() {
        List<CategoryBean> list = mapperX.selectRentIdList() ;
        return list;
    }
}
