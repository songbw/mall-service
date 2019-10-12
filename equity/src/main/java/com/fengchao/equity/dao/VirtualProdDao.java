package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.VirtualProdMapper;
import com.fengchao.equity.mapper.VirtualProdMapperX;
import com.fengchao.equity.mapper.VirtualTicketsMapper;
import com.fengchao.equity.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VirtualProdDao {

    @Autowired
    private VirtualProdMapper prodMapper;
    @Autowired
    private VirtualProdMapperX prodMapperX;
    @Autowired
    private VirtualTicketsMapper ticketsMapper;


    public int createVirtualProd(VirtualProd bean) {
        return prodMapper.insertSelective(bean);
    }

    public int updateVirtualProd(VirtualProd bean) {
        return prodMapper.updateByPrimaryKeySelective(bean);
    }

    public PageInfo<VirtualProd> findVirtualProd(Integer pageNo, Integer pageSize) {
        VirtualProdExample example = new VirtualProdExample();

        PageHelper.startPage(pageNo, pageSize);
        List<VirtualProd> virtualProds = prodMapper.selectByExample(example);
        return new PageInfo<>(virtualProds);
    }

    public VirtualProdX findByVirtualProdId(Integer id) {
        VirtualProdX virtualProdX = prodMapperX.selectByPrimaryKey(id);

        VirtualTicketsExample example = new VirtualTicketsExample();
        VirtualTicketsExample.Criteria criteria = example.createCriteria();
        criteria.andMpuEqualTo(virtualProdX.getMpu());
        List<VirtualTickets> virtualTickets = ticketsMapper.selectByExample(example);
        virtualProdX.setTickets(virtualTickets);

        return virtualProdX;
    }

    public VirtualProdX findByVirtualProdMpu(String mpu) {
        VirtualProdX virtualProdX = prodMapperX.selectByVirtualProdMpu(mpu);
        return virtualProdX;
    }
}
