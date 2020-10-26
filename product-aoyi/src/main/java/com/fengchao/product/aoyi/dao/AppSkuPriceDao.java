package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AppSkuPriceMapper;
import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.model.AppSkuPriceExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
     */
    public List<AppSkuPrice> selectById() {
        AppSkuPriceExample example = new AppSkuPriceExample();
//        AppSkuPriceExample.Criteria criteria = example.createCriteria();
//        criteria.andSpuIdEqualTo(spuId) ;

        return mapper.selectByExample(example);
    }

    public List<AppSkuPrice> selectByRenterIdAndMpuAndSku(AppSkuPrice bean) {
        AppSkuPriceExample example = new AppSkuPriceExample();
        AppSkuPriceExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(bean.getRenterId())) {
            criteria.andRenterIdEqualTo(bean.getRenterId()) ;
        }
        criteria.andMpuEqualTo(bean.getMpu()) ;
        criteria.andSkuIdEqualTo(bean.getSkuId()) ;

        return mapper.selectByExample(example);
    }

    public List<AppSkuPrice> batchSelectByRenterIdAndMpuAndSku(String renterId, List<String> mpus, List<String> skuIds) {
        AppSkuPriceExample example = new AppSkuPriceExample();
        AppSkuPriceExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(renterId)) {
            criteria.andRenterIdEqualTo(renterId) ;
        }
        if (mpus == null || mpus.size() == 0) {
            return new ArrayList<>();
        }
        if (skuIds == null || skuIds.size() == 0) {
            return new ArrayList<>();
        }
        criteria.andMpuIn(mpus) ;
        criteria.andSkuIdIn(skuIds) ;

        return mapper.selectByExample(example);
    }

}
