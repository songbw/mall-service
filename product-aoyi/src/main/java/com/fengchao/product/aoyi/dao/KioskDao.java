package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.mapper.KioskMapper;
import com.fengchao.product.aoyi.mapper.KioskSoltMapper;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskExample;
import com.fengchao.product.aoyi.model.KioskSolt;
import com.fengchao.product.aoyi.model.KioskSoltExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author songbw
 * @date 2021/1/27 7:07
 */
@Component
public class KioskDao {

    private final KioskMapper  mapper;
    private final KioskSoltMapper soltMapper ;

    @Autowired
    public KioskDao(KioskMapper mapper, KioskSoltMapper soltMapper) {
        this.mapper = mapper;
        this.soltMapper = soltMapper;
    }

    public PageInfo<Kiosk> selectByPageable(KioskQueryBean queryBean) {
        KioskExample example = new KioskExample();
        KioskExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(queryBean.getKioskId())) {
            criteria.andEquipmentIdEqualTo(queryBean.getKioskId()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getKioskNo())) {
            criteria.andEquipmentNoEqualTo(queryBean.getKioskNo()) ;
        }
        if (StringUtils.isNotBlank(queryBean.getLocation())) {
            criteria.andLocationEqualTo(queryBean.getLocation()) ;
        }
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<Kiosk> list = mapper.selectByExample(example) ;
        PageInfo<Kiosk> pageInfo = new PageInfo<>(list) ;
        return pageInfo ;
    }

    /**
     * 根据equipment id 查询设备信息
     * @param equipmentId
     * @return
     */
    public Kiosk selectByEquipmentId(String equipmentId) {
        KioskExample example = new KioskExample();
        KioskExample.Criteria criteria = example.createCriteria();
        criteria.andEquipmentIdEqualTo(equipmentId) ;
        List<Kiosk> list = mapper.selectByExample(example) ;
        if (list != null && list.size() > 0) {
            return list.get(0) ;
        }
        return null ;
    }

    /**
     * 根据货道ID获取信息
     * @param slotId
     * @return
     */
    public KioskSolt selectBySlotId(String slotId) {
        KioskSoltExample example = new KioskSoltExample();
        KioskSoltExample.Criteria criteria = example.createCriteria();
        criteria.andSoltIdEqualTo(slotId) ;
        List<KioskSolt> list = soltMapper.selectByExample(example) ;
        if (list != null && list.size() > 0) {
            return list.get(0) ;
        }
        return null ;
    }

    /**
     * 根据设备ID查询货道信息
     * @param kioskId
     * @return
     */
    public List<KioskSolt> selectBykioskId(String kioskId) {
        KioskSoltExample example = new KioskSoltExample();
        KioskSoltExample.Criteria criteria = example.createCriteria();
        criteria.andKioskIdEqualTo(kioskId) ;
        List<KioskSolt> list = soltMapper.selectByExample(example) ;
        return list ;
    }

}
