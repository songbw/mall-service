package com.fengchao.order.dao;

import com.fengchao.order.constants.IStatusEnum;
import com.fengchao.order.mapper.WeipinhuiAddressMapper;
import com.fengchao.order.model.WeipinhuiAddress;
import com.fengchao.order.model.WeipinhuiAddressExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WeipinhuiAddressDao {

    private WeipinhuiAddressMapper weipinhuiAddressMapper;

    @Autowired
    public WeipinhuiAddressDao(WeipinhuiAddressMapper weipinhuiAddressMapper) {
        this.weipinhuiAddressMapper = weipinhuiAddressMapper;
    }

    /**
     * 根据苏宁的地址code和父地址code查询
     *
     * @param snCode
     * @param snPCode
     * @param level
     * @return
     */
    public WeipinhuiAddress selectBySuningAddress(String snCode, String snPCode, Integer level) {
        WeipinhuiAddressExample weipinhuiAddressExample = new WeipinhuiAddressExample();

        WeipinhuiAddressExample.Criteria criteria = weipinhuiAddressExample.createCriteria();

        criteria.andIstatusEqualTo(IStatusEnum.VALID.getCode().shortValue());

        criteria.andSnCodeEqualTo(snCode);
        criteria.andSnPcodeEqualTo(snPCode);
        criteria.andLevelEqualTo(level);

        List<WeipinhuiAddress> weipinhuiAddressList = weipinhuiAddressMapper.selectByExample(weipinhuiAddressExample);

        if (CollectionUtils.isNotEmpty(weipinhuiAddressList)) {
            if (weipinhuiAddressList.size() > 1) {
                throw new RuntimeException("根据苏宁的地址code和父地址code查询 查询结果>1 与期望不符合");
            }

            return weipinhuiAddressList.get(0);
        } else {
            return null;
        }
    }
}
