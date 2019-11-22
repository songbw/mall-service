package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.dao.PlatformDao;
import com.fengchao.product.aoyi.mapper.PlatformMapper;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author songbw
 * @date 2019/11/22 10:26
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
        OperaResponse response = new OperaResponse() ;
        if (StringUtils.isEmpty(bean.getAppId())) {
            response.setCode(200011);
            response.setMsg("appId不能为空。");
            return response ;
        }
        if (bean.getId() == null || bean.getId() == 0) {
            response.setCode(200012);
            response.setMsg("id不能为空或0。");
            return response ;
        }
        Date date = new Date();
        bean.setUpdatedAt(date);
        mapper.updateByPrimaryKeySelective(bean) ;
        response.setData(bean.getId());
        return response;
    }

    @Override
    public OperaResponse findList(QueryBean bean) {
        OperaResponse response = new OperaResponse() ;
        response.setData(dao.selectByListPageble(bean));
        return response;
    }

    @Override
    public OperaResponse find(Integer id) {
        OperaResponse response = new OperaResponse() ;
        if (id == null || id == 0) {
            response.setCode(200012);
            response.setMsg("id不能为空或0。");
            return response ;
        }
        response.setData(mapper.selectByPrimaryKey(id));
        return response;
    }
}
