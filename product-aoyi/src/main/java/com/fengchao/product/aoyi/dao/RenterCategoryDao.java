package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.RenterCategoryQueryBean;
import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.mapper.RenterCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param queryBean
     */
    public List<CategoryBean> selectByRenterId(RenterCategoryQueryBean queryBean) {
        HashMap map = new HashMap() ;
        map.put("appId", queryBean.getAppId()) ;
        map.put("renterId", queryBean.getRenterId()) ;
        List<CategoryBean> list = mapperX.selectRentIdList(map) ;
        return list;
    }
}
