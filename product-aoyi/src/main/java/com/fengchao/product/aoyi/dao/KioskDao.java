package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.mapper.KioskMapper;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskExample;
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

    @Autowired
    public KioskDao(KioskMapper mapper) {
        this.mapper = mapper;
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
}
