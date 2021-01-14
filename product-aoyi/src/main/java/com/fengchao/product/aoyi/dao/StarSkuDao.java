package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StarSkuQueryBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;
import com.fengchao.product.aoyi.constants.IStatusEnum;
import com.fengchao.product.aoyi.mapper.StarSkuMapper;
import com.fengchao.product.aoyi.mapper.StarSkuXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndexExample;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.model.StarSkuExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        example.setOrderByClause("create_time desc");
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据merchantCode查询数量
     * @param merchantCode
     * @return
     */
    public long selectAllCount(String merchantCode) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andMerchantCodeEqualTo(merchantCode) ;
        return starSkuMapper.countByExample(example) ;
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
     * 根据sku查询
     *
     * @param skuId
     * @return
     */
    public StarSku selectBySkuId(String skuId) {
        StarSkuExample starSkuExample = new StarSkuExample();

        StarSkuExample.Criteria criteria = starSkuExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.VALID.getCode().shortValue());

        criteria.andSkuIdEqualTo(skuId);

        List<StarSku> starSkuList = starSkuMapper.selectByExample(starSkuExample);

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
    public void updatePriceByCode(StarSku starSku) {
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
    public List<StarSku> selectBySpuIds(List<String> spuIds, Integer status) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdIn(spuIds) ;
        if (status != -1) {
            criteria.andStatusEqualTo(status) ;
        }
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

    /**
     * 根据code更新上下架状态
     * @param starSku
     */
    public void updateStatusByCode(StarSku starSku) {
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
        StarSku starSku = new StarSku() ;
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
        StarSku starSku = new StarSku() ;
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
    public List<StarSku> selectUBySpuId(String spuId) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId) ;
        criteria.andStatusEqualTo(1) ;
        List<StarSku> list = starSkuMapper.selectByExample(example);
        return list;
    }

    /**
     * 分页查询 star sku
     * @param queryBean
     * @return
     */
    public PageInfo<StarSku> selectPageable(StarSkuQueryBean queryBean) {
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria() ;
        criteria.andMerchantCodeEqualTo(queryBean.getMerchantCode()) ;
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<StarSku> list = starSkuMapper.selectByExample(example);
        PageInfo<StarSku> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 过滤供应商批量查询
     * @param supplyBeans
     * @return
     */
    public List<StarSku> selectProdBySpuIds(List<SupplyBean> supplyBeans) {
        List<SupplyBean> supplyBeanList = supplyBeans.stream().filter(supplyBean -> supplyBean.getSpuId() != supplyBean.getSkuId()).collect(Collectors.toList());
        if (supplyBeanList == null || supplyBeanList.size() == 0) {
            return null ;
        }
        List<String> spuIds = supplyBeanList.stream().map(supplyBean -> supplyBean.getSpuId()).collect(Collectors.toList());
        List<String> skuIds = supplyBeanList.stream().map(supplyBean -> supplyBean.getSkuId()).collect(Collectors.toList());
        StarSkuExample example = new StarSkuExample();
        StarSkuExample.Criteria criteria = example.createCriteria() ;
        criteria.andSpuIdIn(spuIds) ;
        criteria.andCodeIn(skuIds) ;
        List<StarSku> list = starSkuMapper.selectByExample(example) ;
        return list ;
    }

}
