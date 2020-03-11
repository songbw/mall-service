package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.constants.IStatusEnum;
import com.fengchao.product.aoyi.mapper.StarSkuMapper;
import com.fengchao.product.aoyi.mapper.StarSkuXMapper;
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

    private StarSkuMapper starSkuMapper;

    private StarSkuXMapper starSkuXMapper;

    @Autowired
    public StarSkuDao(StarSkuMapper starSkuMapper,
                      StarSkuXMapper starSkuXMapper) {
        this.starSkuMapper = starSkuMapper;
        this.starSkuXMapper = starSkuXMapper;
    }

    /**
     * 批量插入
     *
     * @param starSkuList
     */
    public void batchInsert(List<StarSku> starSkuList) {
        starSkuXMapper.batchInsert(starSkuList);
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
        List<StarSku> list = starSkuMapper.selectByExample(example);
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
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectAll() {
        StarSkuExample example = new StarSkuExample();
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据skuId集合查询
     *
     * @param skuIdList
     * @return
     */
    public List<StarSku> selectBySkuIdList(List<String> skuIdList) {
        StarSkuExample starSkuExample = new StarSkuExample();

        StarSkuExample.Criteria criteria = starSkuExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.VALID.getCode().shortValue());

        criteria.andSkuIdIn(skuIdList);

        List<StarSku> starSkuList = starSkuMapper.selectByExample(starSkuExample);

        return starSkuList;
    }

    /**
     * 根据code跟新价格
     * @param starSku
     */
    public void updatePriceByCode(StarSku starSku) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(starSku.getCode()) ;
        starSkuMapper.updateByExampleSelective(starSku, example);
    }

    /**
     * 根据codeList`查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectByCodeList(List<String> codeList) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeIn(codeList) ;
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectBySpuIds(List<String> spuIds) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdIn(spuIds) ;
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据code查询SKU信息
     *
     * @return
     */
    public List<StarSku> selectByCode(String code) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code) ;
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

}
