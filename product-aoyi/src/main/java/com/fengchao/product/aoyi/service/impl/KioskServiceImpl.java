package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.KioskDao;
import com.fengchao.product.aoyi.mapper.KioskMapper;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.service.KioskService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author songbw
 * @date 2021/1/27 7:20
 */
@Service
public class KioskServiceImpl implements KioskService {

    private final KioskDao dao ;
    private final KioskMapper mapper ;

    @Autowired
    public KioskServiceImpl(KioskDao dao, KioskMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public OperaResponse findByPageable(KioskQueryBean queryBean) {
        OperaResponse response = new OperaResponse() ;
        PageInfo<Kiosk> pageInfo = dao.selectByPageable(queryBean) ;
        response.setData(pageInfo);
        return response;
    }

    @Override
    public OperaResponse add(Kiosk kiosk) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        kiosk.setCreatedAt(date);
        kiosk.setUpdatedAt(date);
        mapper.insertSelective(kiosk) ;
        return response;
    }

    @Override
    public OperaResponse update(Kiosk kiosk) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        kiosk.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(kiosk) ;
        return response;
    }

    @Override
    public OperaResponse delete(Integer id) {
        OperaResponse response = new OperaResponse() ;
        mapper.deleteByPrimaryKey(id) ;
        return response;
    }

    @Override
    public OperaResponse find(Integer id) {
        OperaResponse response = new OperaResponse() ;
        Kiosk kiosk = mapper.selectByPrimaryKey(id) ;
        response.setData(kiosk);
        return response;
    }
}
