package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.dao.PlatformDao;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author songbw
 * @date 2019/11/26 17:09
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformDao dao ;

    @Override
    public Platform findByAppId(String appId) {
        return dao.selectByAppId(appId);
    }
}
