package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.model.Platform;

/**
 * @author songbw
 * @date 2019/11/26 17:08
 */
public interface PlatformService {
    Platform findByAppId(String appId) ;
}
