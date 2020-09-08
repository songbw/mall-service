package com.fengchao.product.aoyi.dao;

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

    @Autowired
    public RenterCategoryDao(RenterCategoryMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<RenterCategory> selectById(Integer spuId) {
        RenterCategoryExample example = new RenterCategoryExample();
        RenterCategoryExample.Criteria criteria = example.createCriteria();
//        criteria.andSpuIdEqualTo(spuId) ;

        List<RenterCategory> list = mapper.selectByExample(example);

        return list;
    }
}
