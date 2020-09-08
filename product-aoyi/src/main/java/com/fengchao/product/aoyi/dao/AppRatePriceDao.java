package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AppRatePriceMapper;
import com.fengchao.product.aoyi.mapper.RenterCategoryMapper;
import com.fengchao.product.aoyi.model.AppRatePrice;
import com.fengchao.product.aoyi.model.AppRatePriceExample;
import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.model.RenterCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class AppRatePriceDao {

    private AppRatePriceMapper mapper;

    @Autowired
    public AppRatePriceDao(AppRatePriceMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<AppRatePrice> selectById(Integer spuId) {
        AppRatePriceExample example = new AppRatePriceExample();
        AppRatePriceExample.Criteria criteria = example.createCriteria();
//        criteria.andSpuIdEqualTo(spuId) ;

        List<AppRatePrice> list = mapper.selectByExample(example);

        return list;
    }
}
