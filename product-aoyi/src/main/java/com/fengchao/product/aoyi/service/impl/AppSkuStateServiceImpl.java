package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.AppSkuStateDao;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.mapper.AppSkuStateMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AppSkuState;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.service.AppSkuStateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2020/9/8 18:22
 */
@Service
@Slf4j
public class AppSkuStateServiceImpl implements AppSkuStateService {

    @Autowired
    private AppSkuStateMapper mapper ;
    @Autowired
    private AppSkuStateDao dao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private StarSkuDao starSkuDao ;


    @Override
    public List<AppSkuState> findListById(int id) {
        return null;
    }

    @Override
    public OperaResponse add(AppSkuState bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        if (StringUtils.isBlank(bean.getRenterId())) {
            response.setCode(1000000);
            response.setMsg("renterId 不能为null");
            return response;
        }
        List<AppSkuState> list = dao.selectByRenterIdAndMpuAndSku(bean) ;
        if (list != null && list.size() > 0) {
            bean.setId(list.get(0).getId());
            bean.setUpdatedAt(date);
            mapper.updateByPrimaryKeySelective(bean) ;
        } else {
            bean.setUpdatedAt(date);
            bean.setCreatedAt(date);
            mapper.insertSelective(bean) ;
        }

        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse addBatch(List<AppSkuState> beans) {
        OperaResponse response = new OperaResponse() ;
        List<AppSkuState> errors = new ArrayList<>() ;
        beans.forEach(bean -> {
            Date date = new Date();
            if (StringUtils.isBlank(bean.getRenterId())) {
                errors.add(bean) ;
            } else {
                List<AppSkuState> list = dao.selectByRenterIdAndMpuAndSku(bean) ;
                if (list != null && list.size() > 0) {
                    bean.setId(list.get(0).getId());
                    bean.setUpdatedAt(date);
                    mapper.updateByPrimaryKeySelective(bean) ;
                } else {
                    bean.setUpdatedAt(date);
                    bean.setCreatedAt(date);
                    mapper.insertSelective(bean) ;
                }
            }
        });
        if (errors != null && errors.size() > 0) {
            response.setData(errors);
        } else {
            response.setData(beans);
        }
        return response;
    }

    @Override
    public OperaResponse update(AppSkuState bean) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        bean.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(bean) ;
        response.setData(bean);
        return response;
    }

    @Override
    public OperaResponse updateBatchState(List<AppSkuState> beans) {
        OperaResponse response = new OperaResponse() ;
        /**
         * 分四种情况
         * 1、主商品为上架状态，子商品也是上架状态（状态相同，不处理）
         * 2、主商品为下架状态，子商品也是下架状态（状态相同，不处理）
         * 3、主商品为上架状态，子商品为下架状态（添加一条子商品为下架状态的记录）
         * 4、主商品为下架状态，子商品为上架状态（子商品记录删除）
         */
        Date date = new Date();
        beans.forEach(bean -> {
            // 判断添加，还是删除
            int checkState = 0 ;
            if (bean.getMpu().equals(bean.getSkuId())) {
                AoyiProdIndex aoyiProdIndex = productDao.selectByMpu(bean.getMpu()) ;
                if (aoyiProdIndex != null) {
                    checkState = checkState(Integer.parseInt(aoyiProdIndex.getState()), bean.getState()) ;
                }
            } else {
                List<StarSku> starSkus = starSkuDao.selectBySpuIdAndCode(bean.getMpu(), bean.getSkuId()) ;
                if (starSkus != null && starSkus.size() > 0) {
                    StarSku starSku = starSkus.get(0) ;
                    checkState = checkState(starSku.getStatus(), bean.getState()) ;
                }
            }
            if (checkState == 3) {
                List<AppSkuState> appSkuStates = dao.selectByRenterIdAndMpuAndSku(bean) ;
                if (appSkuStates != null && appSkuStates.size() > 0) {
                    AppSkuState appSkuState = appSkuStates.get(0) ;
                    // 更新
                    if (appSkuState.getState() != bean.getState()) {
                        bean.setUpdatedAt(date);
                        mapper.updateByPrimaryKeySelective(bean) ;
                    }
                } else {
                    // insert
                    bean.setCreatedAt(date);
                    bean.setUpdatedAt(date);
                    mapper.insertSelective(bean) ;
                }
            }
            if (checkState == 4) {
                List<AppSkuState> appSkuStates = dao.selectByRenterIdAndMpuAndSku(bean) ;
                if (appSkuStates != null && appSkuStates.size() > 0) {
                    AppSkuState appSkuState = appSkuStates.get(0) ;
                    mapper.deleteByPrimaryKey(appSkuState.getId()) ;
                }
            }
        });
        response.setData(beans);
        return response;
    }

    /**
     * 上下架状态 0：下架；1：上架
     * 分四种情况
     * 1、主商品为上架状态，子商品也是上架状态（状态相同，不处理）
     * 2、主商品为下架状态，子商品也是下架状态（状态相同，不处理）
     * 3、主商品为上架状态，子商品为下架状态（添加一条子商品为下架状态的记录）
     * 4、主商品为下架状态，子商品为上架状态（子商品记录删除）
     */
    private int checkState(int prodState, int renterState) {
        if (prodState == 1 && renterState == 0) {
            return 3;
        }
        if (prodState == 0 && renterState == 1) {
            return 4;
        }
        return 0;
    }

    @Override
    public List<AppSkuState> findByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id) ;
    }
}
