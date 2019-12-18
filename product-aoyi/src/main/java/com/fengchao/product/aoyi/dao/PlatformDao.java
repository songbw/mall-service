package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.mapper.PlatformMapper;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.model.PlatformExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author songbw
 * @Date 19-10-10 下午5:41
 */
@Component
public class PlatformDao {

    private PlatformMapper mapper;

    @Autowired
    public PlatformDao(PlatformMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据appId查询平台信息
     *
     * @return
     */
    public Platform selectByAppId(String appId) {
        PlatformExample example = new PlatformExample();

        PlatformExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId) ;

        List<Platform> list = mapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0) ;
        }
        return null;
    }

    /**
     * 查询分页信息
     * @return queryBean
     */
    public PageInfo<Platform> selectByListPageble(QueryBean queryBean) {
        PlatformExample example = new PlatformExample();
        PlatformExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(queryBean.getPageNo(), queryBean.getPageSize());
        List<Platform> list = mapper.selectByExample(example);
        PageInfo<Platform> pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
