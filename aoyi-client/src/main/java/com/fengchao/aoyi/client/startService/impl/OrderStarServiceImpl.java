package com.fengchao.aoyi.client.startService.impl;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.config.StarClientConfig;
import com.fengchao.aoyi.client.starBean.StarOrderBean;
import com.fengchao.aoyi.client.startService.OrderStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author songbw
 * @date 2020/1/3 16:21
 */
@EnableConfigurationProperties({StarClientConfig.class})
@Slf4j
@Service
public class OrderStarServiceImpl implements OrderStarService {
    @Override
    public OperaResponse addOrder(StarOrderBean bean) {
        return null;
    }
}
