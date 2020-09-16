package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AppSkuPriceMapper;
import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.model.AppSkuPriceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class AppSkuPriceDao {

    private AppSkuPriceMapper mapper;

    @Autowired
    public AppSkuPriceDao(AppSkuPriceMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<AppSkuPrice> selectById(Integer spuId) {
        AppSkuPriceExample example = new AppSkuPriceExample();
        AppSkuPriceExample.Criteria criteria = example.createCriteria();
//        criteria.andSpuIdEqualTo(spuId) ;

        List<AppSkuPrice> list = mapper.selectByExample(example);

        return list;
    }
}
