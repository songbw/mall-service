package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.constants.IStatusEnum;
import com.fengchao.product.aoyi.mapper.StarSkuMapper;
import com.fengchao.product.aoyi.mapper.StarSkuXMapper;
import com.fengchao.product.aoyi.model.StarSkuBean;
import com.fengchao.product.aoyi.model.StarSkuExample;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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
    public void batchInsert(List<StarSkuBean> starSkuList) {
        starSkuXMapper.batchInsert(starSkuList);
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectBySpuId(String spuId) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectBySpuIdAndCode(String spuId, String code) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        criteria.andCodeEqualTo(code) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectAll() {
        StarSkuExample example = new StarSkuExample();
        example.setOrderByClause("create_time desc");
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据skuId集合查询
     *
     * @param skuIdList
     * @return
     */
    public List<StarSkuBean> selectBySkuIdList(List<String> skuIdList) {
        StarSkuExample starSkuExample = new StarSkuExample();

        StarSkuExample.Criteria criteria = starSkuExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.VALID.getCode().shortValue());

        criteria.andSkuIdIn(skuIdList);

        List<StarSkuBean> starSkuList = starSkuMapper.selectByExample(starSkuExample);

        return starSkuList;
    }

    /**
     * 根据sku查询
     *
     * @param skuId
     * @return
     */
    public StarSkuBean selectBySkuId(String skuId) {
        StarSkuExample starSkuExample = new StarSkuExample();

        StarSkuExample.Criteria criteria = starSkuExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.VALID.getCode().shortValue());

        criteria.andSkuIdEqualTo(skuId);

        List<StarSkuBean> starSkuList = starSkuMapper.selectByExample(starSkuExample);

        if (CollectionUtils.isEmpty(starSkuList)) {
            return null;
        } else if (starSkuList.size() > 1) {
            throw new RuntimeException("根据sku查询 数据库结果大于1 与期望不符 skuId:" + skuId);
        }

        return starSkuList.get(0);
    }

    /**
     * 根据code跟新价格
     * @param starSku
     */
    public void updatePriceByCode(StarSkuBean starSku) {
        starSku.setUpdateTime(new Date());
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
    public List<StarSkuBean> selectByCodeList(List<String> codeList) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeIn(codeList) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据spuId查询SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectBySpuIds(List<String> spuIds) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdIn(spuIds) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据code查询SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectByCode(String code) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据code更新上下架状态
     * @param starSku
     */
    public void updateStatusByCode(StarSkuBean starSku) {
        starSku.setUpdateTime(new Date());
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(starSku.getCode()) ;
        starSkuMapper.updateByExampleSelective(starSku, example);
    }

    /**
     * 根据code和spuId更新上下架状态
     * @param bean
     */
    public void updateStatusByCodeAndSpuId(StateBean bean) {
        StarSkuBean starSku = new StarSkuBean() ;
        starSku.setCode(bean.getSkuId());
        starSku.setSpuId(bean.getSpuId());
        starSku.setStatus(Integer.valueOf(bean.getState()));
        starSku.setUpdateTime(new Date());
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(starSku.getCode()) ;
        criteria.andSpuIdEqualTo(starSku.getSpuId()) ;
        starSkuMapper.updateByExampleSelective(starSku, example);
    }

    /**
     * 根据code和spuId更新上下架价格
     * @param bean
     */
    public void updatePriceByCodeAndSpuId(PriceBean bean) {
        StarSkuBean starSku = new StarSkuBean() ;
        starSku.setCode(bean.getSkuId());
        starSku.setSpuId(bean.getSpuId());
        // 建议销售价
        BigDecimal bigDecimal = new BigDecimal(bean.getPrice()) ;
        int advisePrice = bigDecimal.multiply(new BigDecimal("100")).intValue() ;
        starSku.setAdvisePrice(advisePrice);
        // 进货价
        BigDecimal bigDecimalS = new BigDecimal(bean.getSPrice()) ;
        int sprice = bigDecimalS.multiply(new BigDecimal("100")).intValue() ;
        starSku.setSprice(sprice);
        // 计算销售价
        BigDecimal bigDecimalPrice = new BigDecimal(0) ;
        bigDecimalPrice = bigDecimalS.divide(new BigDecimal(0.9), 2, BigDecimal.ROUND_HALF_UP) ;
        int price = bigDecimalPrice.multiply(new BigDecimal("100")).intValue() ;
        if (price > advisePrice) {
            starSku.setPrice(price);
        } else {
            starSku.setPrice(advisePrice);
        }
        starSku.setUpdateTime(new Date());
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(starSku.getCode()) ;
        criteria.andSpuIdEqualTo(starSku.getSpuId()) ;
        starSkuMapper.updateByExampleSelective(starSku, example);
    }

    /**
     * 根据spuId查询上架SKU信息
     *
     * @return
     */
    public List<StarSkuBean> selectUBySpuId(String spuId) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        criteria.andStatusEqualTo(1) ;
        List<StarSkuBean> list = starSkuMapper.selectByExample(example);
        return list;
    }

}
