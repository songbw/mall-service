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
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2019/11/26 17:09
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformMapper mapper ;
    @Autowired
    private PlatformDao dao ;

    @Override
    public OperaResponse add(Platform bean) {
        OperaResponse response = new OperaResponse() ;
        if (StringUtils.isEmpty(bean.getAppId())) {
            response.setCode(200011);
            response.setMsg("appId不能为空。");
            return response ;
        }
        Platform temp = dao.selectByAppId(bean.getAppId()) ;
        if (temp != null) {
            response.setCode(200010);
            response.setMsg("appId 已存在。");
            return response ;
        }
        Date date = new Date();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        mapper.insertSelective(bean) ;
        response.setData(bean.getId());
        return response;
    }

    @Override
    public OperaResponse delete(Integer id) {
        return null;
    }

    @Override
    public OperaResponse modify(Platform bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getAppId())) {
            response.setCode(200011);
            response.setMsg("appId不能为空。");
            return response;
        }
        if (bean.getId() == null || bean.getId() == 0) {
            response.setCode(200012);
            response.setMsg("id不能为空或0。");
            return response;
        }
        bean.setAppId(null);
        Date date = new Date();
        bean.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(bean);
        response.setData(bean.getId());
        return response;
    }

    @Override
    public Platform findByAppId(String appId) {
        return dao.selectByAppId(appId);
    }

    @Override
    public OperaResponse findList(QueryBean bean) {
        OperaResponse response = new OperaResponse();
        PageInfo<Platform> pageInfo = dao.selectByListPageble(bean) ;
        response.setData(pageInfo);
        return response;
    }

    @Override
    public OperaResponse find(Integer id) {
        OperaResponse response = new OperaResponse();
        response.setData(mapper.selectByPrimaryKey(id)) ;
        return response;
    }

    @Override
    public Platform findBySubAppId(String appId) {
        Platform sub = dao.selectByAppId(appId) ;
        return mapper.selectByPrimaryKey(sub.getParentId());
    }

    @Override
    public List<Platform> findByAppIdList(List<String> appIdList) {
        List<Platform> platformList = dao.selectByAppIdList(appIdList);
        return platformList;
    }
}
