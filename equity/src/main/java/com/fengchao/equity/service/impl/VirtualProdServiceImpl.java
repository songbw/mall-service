package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.VirtualProdDao;
import com.fengchao.equity.dao.VirtualTicketsDao;
import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.model.VirtualProdX;
import com.fengchao.equity.service.VirtualProdService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualProdServiceImpl implements VirtualProdService {

    @Autowired
    private VirtualProdDao prodDao;
    @Autowired
    private VirtualTicketsDao ticketsDao;

    @Override
    public int createVirtualProd(VirtualProd bean) {
        return prodDao.createVirtualProd(bean);
    }

    @Override
    public PageableData<VirtualProd> findVirtualProd(Integer pageNo, Integer pageSize) {
        PageableData<VirtualProd> pageableData = new PageableData<>();
        PageInfo<VirtualProd> pageInfo = prodDao.findVirtualProd(pageNo, pageSize);

        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<VirtualProd> virtualProdList = pageInfo.getList();
        pageableData.setList(virtualProdList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public VirtualProdX findByVirtualProdId(Integer id) {
        VirtualProdX virtualProd = prodDao.findByVirtualProdId(id);
        virtualProd.setTickets(ticketsDao.findVirtualTicketByMpu(virtualProd.getMpu()));
        return virtualProd;
    }

    @Override
    public int updateVirtualProd(VirtualProd bean) {
        return prodDao.updateVirtualProd(bean);
    }

    @Override
    public VirtualProdX findByVirtualProdMpu(String mpu) {
        VirtualProdX virtualProdX = prodDao.findByVirtualProdMpu(mpu);
        virtualProdX.setTickets(ticketsDao.findVirtualTicketByMpu(mpu));
        return virtualProdX;
    }

    @Override
    public int deleteVirtualProd(String mpu) {
        return prodDao.deleteVirtualProd(mpu);
    }

    @Override
    public VirtualProdX findVirtualProdInfoByMpu(String mpu) {
        return prodDao.findByVirtualProdMpu(mpu);
    }

}
