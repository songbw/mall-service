package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.StarPropertyMapper;
import com.fengchao.product.aoyi.model.StarProperty;
import com.fengchao.product.aoyi.model.StarPropertyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class StarPropertyDao {

    private StarPropertyMapper mapper;

    @Autowired
    public StarPropertyDao(StarPropertyMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据productId和type查询属性信息
     *
     * @return
     */
    public List<StarProperty> selectByProductIdAndType(Integer productId, Integer type) {
        StarPropertyExample example = new StarPropertyExample();
        StarPropertyExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId) ;
        criteria.andTypeEqualTo(type) ;
        List<StarProperty> list = mapper.selectByExample(example);
        return list;
    }
}
