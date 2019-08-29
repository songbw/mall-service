package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PriceBean;
import com.fengchao.product.aoyi.bean.StateBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.ThirdProdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


@Service
public class ThirdProdServiceImpl implements ThirdProdService {

    private static Logger logger = LoggerFactory.getLogger(ThirdProdServiceImpl.class);

    @Autowired
    private AoyiProdIndexXMapper prodMapper;
    @Autowired
    private ProductDao productDao;

    @Override
    public OperaResult add(AoyiProdIndexX bean){
        OperaResult result = new OperaResult();
        // TODO sku不能为空，不能重复
        if (StringUtils.isEmpty(bean.getSkuid())){
            result.setCode(2000001);
            result.setMsg("skuid 不能为空。");
            return result;
        }
        // TODO brand不能为空
        if (StringUtils.isEmpty(bean.getBrand())){
            result.setCode(2000002);
            result.setMsg("brand 不能为空。");
            return result;
        }
        // TODO category不能为空
        if (StringUtils.isEmpty(bean.getCategory())){
            result.setCode(2000003);
            result.setMsg("category 不能为空。");
            return result;
        }
        // TODO model不能为空
        if (StringUtils.isEmpty(bean.getModel())){
            result.setCode(2000004);
            result.setMsg("model 不能为空。");
            return result;
        }
        // TODO name不能为空
        if (StringUtils.isEmpty(bean.getName())){
            result.setCode(2000005);
            result.setMsg("name 不能为空。");
            return result;
        }
        // TODO price不能为空
        if (StringUtils.isEmpty(bean.getPrice())){
            result.setCode(2000006);
            result.setMsg("price 不能为空。");
            return result;
        }
        // TODO sprice不能为空
        if (StringUtils.isEmpty(bean.getSprice())){
            result.setCode(2000007);
            result.setMsg("sprice 不能为空。");
            return result;
        }
        // TODO state不能为空
        if (StringUtils.isEmpty(bean.getState())){
            result.setCode(2000008);
            result.setMsg("state 不能为空。");
            return result;
        }
        // TODO 主图不能为空
        if (bean.getZtImage() == null || bean.getZtImage().size() <= 0){
            result.setCode(2000009);
            result.setMsg("ztImage 不能为空。");
            return result;
        }
        // TODO 详情图不能为空
        if (bean.getXqImage() == null || bean.getXqImage().size() <= 0){
            result.setCode(2000010);
            result.setMsg("xqImage 不能为空。");
            return result;
        }
        List<AoyiProdIndex> aoyiProdIndices =  productDao.findBySkuId(bean.getSkuid(), 2);
        if (aoyiProdIndices.size() > 0) {
            result.setCode(2000011);
            result.setMsg("skuid 重复。");
            return result;
        }
        Date date = new Date();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setMpu(bean.getSkuid());
        bean.setMerchantId(2);
        // TODO 组装主图字段，添加图片对呀表
        // TODO 组装详情图字段，添加图片对应表
        // TODO 插入商品信息
        return result;
    }

    @Override
    public OperaResult update(AoyiProdIndexX bean) {
        OperaResult result = new OperaResult();
        // TODO sku不能为空
        if (StringUtils.isEmpty(bean.getSkuid())){
            result.setCode(2000001);
            result.setMsg("skuid 不能为空。");
            return result;
        }
        // TODO 组装主图字段，添加图片对呀表
        if (bean.getZtImage() != null && bean.getZtImage().size() > 0){
            result.setCode(2000010);
            result.setMsg("xqImage 不能为空。");
            return result;
        }
        // TODO 组装详情图字段，添加图片对应表
        if (bean.getXqImage() != null && bean.getXqImage().size() > 0){
            result.setCode(2000010);
            result.setMsg("xqImage 不能为空。");
            return result;
        }
        // TODO 修改商品信息
        return result;
    }

    @Override
    public void updatePrice(PriceBean bean) {
        productDao.updatePrice(bean);
    }

    @Override
    public void updateState(StateBean bean) {
        productDao.updateState(bean);
    }

    @Override
    public void delete(Integer merchantId, Integer id) throws ProductException {
        if (id > 0) {
            prodMapper.deleteByPrimaryKey(id);
        } else {
            throw new ProductException(200002, "id为null或等于0");
        }
    }

}
