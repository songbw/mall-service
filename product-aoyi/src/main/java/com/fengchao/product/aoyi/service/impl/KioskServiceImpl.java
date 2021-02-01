package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.KioskBean;
import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.KioskDao;
import com.fengchao.product.aoyi.mapper.KioskMapper;
import com.fengchao.product.aoyi.mapper.KioskSoltMapper;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskImg;
import com.fengchao.product.aoyi.model.KioskSolt;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.service.KioskService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2021/1/27 7:20
 */
@Service
public class KioskServiceImpl implements KioskService {

    private final KioskDao dao ;
    private final KioskMapper mapper ;
    private final AoyiClientRpcService aoyiClientRpcService ;
    private final KioskSoltMapper soltMapper ;

    @Autowired
    public KioskServiceImpl(KioskDao dao, KioskMapper mapper, AoyiClientRpcService aoyiClientRpcService, KioskSoltMapper soltMapper) {
        this.dao = dao;
        this.mapper = mapper;
        this.aoyiClientRpcService = aoyiClientRpcService;
        this.soltMapper = soltMapper;
    }

    @Override
    public OperaResponse findByPageable(KioskQueryBean queryBean) {
        OperaResponse response = new OperaResponse() ;
        PageInfo<KioskBean> pageInfoBean = new PageInfo<>() ;
        PageInfo<Kiosk> pageInfo = dao.selectByPageable(queryBean) ;
        List<Kiosk> kiosks = pageInfo.getList() ;
        List<KioskBean> kioskBeans = new ArrayList<>();
        BeanUtils.copyProperties(pageInfo, pageInfoBean);
        kiosks.forEach(kiosk -> {
            List<KioskImg> imgs = dao.selectByKioskId(kiosk.getId()) ;
            KioskBean kioskBean = new KioskBean() ;
            BeanUtils.copyProperties(kiosk, kioskBean);
            kioskBean.setImgs(imgs);
            kioskBeans.add(kioskBean) ;
        });
        pageInfoBean.setList(kioskBeans);
        response.setData(pageInfoBean);
        return response;
    }

    @Override
    public OperaResponse add(Kiosk kiosk) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        kiosk.setCreatedAt(date);
        kiosk.setUpdatedAt(date);
        mapper.insertSelective(kiosk) ;
        response.setData(kiosk);
        return response;
    }

    @Override
    public OperaResponse update(Kiosk kiosk) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        kiosk.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(kiosk) ;
        response.setData(kiosk);
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
        KioskBean kioskBean = new KioskBean() ;
        Kiosk kiosk = mapper.selectByPrimaryKey(id) ;
        BeanUtils.copyProperties(kiosk, kioskBean);
        List<KioskImg> imgs = dao.selectByKioskId(kiosk.getId()) ;
        kioskBean.setImgs(imgs);
        response.setData(kioskBean);
        return response;
    }

    @Override
    public OperaResponse syncFusionKiosk() {
        List<Kiosk> kiosks =  aoyiClientRpcService.getKiosks() ;
        Date date = new Date() ;
        kiosks.forEach(kiosk -> {
            Kiosk temp = dao.selectByEquipmentId(kiosk.getEquipmentId()) ;
            if (temp == null) {
                // 添加
                kiosk.setCreatedAt(date);
                kiosk.setUpdatedAt(date);
                mapper.insertSelective(kiosk) ;
            } else {
                // 修改
                kiosk.setUpdatedAt(date);
                kiosk.setId(temp.getId());
                mapper.updateByPrimaryKey(kiosk) ;
            }
        });
        OperaResponse response = new OperaResponse() ;
        response.setData(kiosks);
        return response ;
    }

    @Override
    public OperaResponse syncFusionKioskSlot(String status) {
        List<KioskSolt> kioskSolts =  aoyiClientRpcService.getKioskSlotState(status) ;
        Date date = new Date() ;
        kioskSolts.forEach(kioskSolt -> {
            KioskSolt temp = dao.selectBySlotId(kioskSolt.getKioskId()) ;
            if (temp == null) {
                // 添加
                kioskSolt.setCreatedAt(date);
                kioskSolt.setUpdatedAt(date);
                soltMapper.insertSelective(kioskSolt) ;
            } else {
                // 修改
                kioskSolt.setUpdatedAt(date);
                kioskSolt.setId(temp.getId());
                soltMapper.updateByPrimaryKey(kioskSolt) ;
            }
        });
        OperaResponse response = new OperaResponse() ;
        response.setData(kioskSolts);
        return response ;
    }

    @Override
    public OperaResponse findKioskSlotByKioskId(String kioskId) {
        List<KioskSolt> kioskSolts = dao.selectBykioskId(kioskId) ;
        OperaResponse response = new OperaResponse() ;
        response.setData(kioskSolts);
        return response;
    }

    @Override
    public OperaResponse deleteKioskSlot(Integer id) {
        OperaResponse response = new OperaResponse() ;
        soltMapper.deleteByPrimaryKey(id) ;
        return response;
    }

    @Override
    public OperaResponse addKioskSlot(KioskSolt solt) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        solt.setCreatedAt(date);
        solt.setUpdatedAt(date);
        soltMapper.insertSelective(solt) ;
        response.setData(solt);
        return response;
    }

    @Override
    public OperaResponse updateKioskSlot(KioskSolt solt) {
        OperaResponse response = new OperaResponse() ;
        Date date = new Date();
        solt.setUpdatedAt(date);
        soltMapper.updateByPrimaryKeySelective(solt) ;
        response.setData(solt);
        return response;
    }

    @Override
    public OperaResponse findKioskSlot(Integer id) {
        OperaResponse response = new OperaResponse() ;
        KioskSolt solt = soltMapper.selectByPrimaryKey(id) ;
        response.setData(solt);
        return response;
    }
}
