package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AppSkuStateMapper;
import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.model.AppSkuPriceExample;
import com.fengchao.product.aoyi.model.AppSkuState;
import com.fengchao.product.aoyi.model.AppSkuStateExample;
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
public class AppSkuStateDao {

    private AppSkuStateMapper mapper;

    @Autowired
    public AppSkuStateDao(AppSkuStateMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<AppSkuState> selectById(Integer spuId) {
        AppSkuStateExample example = new AppSkuStateExample();
        AppSkuStateExample.Criteria criteria = example.createCriteria();
//        criteria.andSpuIdEqualTo(spuId) ;

        List<AppSkuState> list = mapper.selectByExample(example);

        return list;
    }

    public List<AppSkuState> selectByRenterIdAndMpuAndSku(AppSkuState bean) {
        AppSkuStateExample example = new AppSkuStateExample();
        AppSkuStateExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(bean.getRenterId())) {
            criteria.andRenterIdEqualTo(bean.getRenterId()) ;
        }
        criteria.andMpuEqualTo(bean.getMpu()) ;
        criteria.andSkuIdEqualTo(bean.getSkuId()) ;

        List<AppSkuState> list = mapper.selectByExample(example);

        return list;
    }

    public List<AppSkuState> batchSelectByRenterIdAndMpuAndSku(String renterId, List<String> mpus, List<String> skuIds) {
        AppSkuStateExample example = new AppSkuStateExample();
        AppSkuStateExample.Criteria criteria = example.createCriteria();
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
