package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.StarDetailImgMapper;
import com.fengchao.product.aoyi.model.StarDetailImg;
import com.fengchao.product.aoyi.model.StarDetailImgExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 20-2-4 下午5:41
 */
@Component
public class StarDetailImgDao {

    private StarDetailImgMapper mapper;

    @Autowired
    public StarDetailImgDao(StarDetailImgMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据spuId查询图片详情
     *
     * @return
     */
    public List<StarDetailImg> selectBySpuId(Integer spuId) {
        StarDetailImgExample example = new StarDetailImgExample();
        StarDetailImgExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;

        List<StarDetailImg> list = mapper.selectByExample(example);

        return list;
    }
}
