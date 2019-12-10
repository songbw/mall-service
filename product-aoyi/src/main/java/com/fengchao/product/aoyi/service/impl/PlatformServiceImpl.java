package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.dao.PlatformDao;
import com.fengchao.product.aoyi.mapper.PlatformMapper;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.service.PlatformService;
import com.github.pagehelper.PageInfo;
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
    @Autowired
    private PlatformMapper mapper ;

    @Override
    public Platform findByAppId(String appId) {
        return dao.selectByAppId(appId);
    }

    @Override
    public OperaResponse findList(QueryBean bean) {
        OperaResponse response = new OperaResponse();
        PageInfo<Platform> pageInfo = dao.selectAllPageable(bean) ;
        response.setData(pageInfo);
        return response;
    }

    @Override
    public Platform findBySubAppId(String appId) {
        Platform sub = dao.selectByAppId(appId) ;
        return mapper.selectByPrimaryKey(sub.getParentId());
    }
}
