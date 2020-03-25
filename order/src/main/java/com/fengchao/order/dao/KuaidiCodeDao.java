package com.fengchao.order.dao;

import com.fengchao.order.mapper.KuaidiCodeMapper;
import com.fengchao.order.mapper.OrderDetailMapper;
import com.fengchao.order.mapper.OrdersMapper;
import com.fengchao.order.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-17 下午3:12
 */
@Component
@Slf4j
public class KuaidiCodeDao {

    private KuaidiCodeMapper kuaidiCodeMapper;



    @Autowired
    public KuaidiCodeDao(KuaidiCodeMapper kuaidiCodeMapper) {
        this.kuaidiCodeMapper = kuaidiCodeMapper;
    }

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    public KuaidiCode selectByKudiName(String name) {
        KuaidiCodeExample kuaidiCodeExample = new KuaidiCodeExample() ;
        KuaidiCodeExample.Criteria kuaidiCriteria = kuaidiCodeExample.createCriteria();

        kuaidiCriteria.andNameEqualTo(name);
        List<KuaidiCode> kuaidiCodeList = kuaidiCodeMapper.selectByExample(kuaidiCodeExample);

        if (CollectionUtils.isEmpty(kuaidiCodeList)) {
            return null;
        } else if (kuaidiCodeList.size() > 1) {
            log.error("根据名称查询快递comcode 不唯一:{}", name);
        }

        return kuaidiCodeList.get(0);
    }

}
