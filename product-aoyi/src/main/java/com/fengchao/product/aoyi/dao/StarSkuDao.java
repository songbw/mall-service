package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.StarSkuMapper;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.model.StarSkuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class StarSkuDao {

    private StarSkuMapper mapper;

    @Autowired
    public StarSkuDao(StarSkuMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectBySpuId(String spuId) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        List<StarSku> list = mapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectBySpuIdAndCode(String spuId, String code) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        criteria.andCodeEqualTo(code) ;
        List<StarSku> list = mapper.selectByExample(example);
        return list;
    }
}
