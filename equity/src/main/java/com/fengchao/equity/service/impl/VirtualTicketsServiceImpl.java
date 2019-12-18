package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.VirtualTicketsBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.VirtualProdDao;
import com.fengchao.equity.dao.VirtualTicketsDao;
import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.model.VirtualProdX;
import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.model.VirtualTicketsX;
import com.fengchao.equity.service.VirtualTicketsService;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.DataUtils;
import com.fengchao.equity.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VirtualTicketsServiceImpl implements VirtualTicketsService {

    @Autowired
    private VirtualProdDao prodDao;
    @Autowired
    private VirtualTicketsDao ticketsDao;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private Environment environment;

    @Override
    public int createVirtualticket(VirtualTicketsBean bean) {
        VirtualProdX virtualProd = prodDao.findByVirtualProdMpu(bean.getMpu());
        if(virtualProd == null){
            return 0;
        }
        VirtualTickets virtualTickets = new VirtualTickets();
        Date now = new Date();
        Integer random = (int)Math.random() * 1000000;
        String code = bean.getMpu() + System.currentTimeMillis() + random;
        virtualTickets.setMpu(bean.getMpu());
        virtualTickets.setOpenId(bean.getOpenId());
        virtualTickets.setOrderId(bean.getOrderId());
        virtualTickets.setNum(bean.getNum());
        virtualTickets.setCode(code);
        virtualTickets.setCreateTime(now);
        virtualTickets.setStartTime(now);
        Date fetureDate = DataUtils.getFetureDate(now, virtualProd.getEffectiveDays());
        if(virtualProd.getEffectiveDays() != -1){
            virtualTickets.setEndTime(fetureDate);
        }
        int virtualticket = ticketsDao.createVirtualticket(virtualTickets);
        if(virtualticket != 0 && virtualProd.getEffectiveDays() != -1){
            JobClientUtils.virtualTicketsInvalidTrigger(environment, jobClient, virtualTickets.getId(), fetureDate);
        }
        return virtualTickets.getId();
    }

    @Override
    public int consumeTicket(VirtualTicketsBean bean) {
        return ticketsDao.consumeTicket(bean);
    }

    @Override
    public int cancelTicket(VirtualTicketsBean bean) {
        return ticketsDao.cancelTicket(bean);
    }

    @Override
    public PageableData<VirtualTicketsX> findVirtualTicket(String openId, Integer pageNo, Integer pageSize) {
        PageableData<VirtualTicketsX> pageableData = new PageableData<>();
        PageInfo<VirtualTicketsX> pageInfo = ticketsDao.findVirtualTicket(openId, pageNo, pageSize);
        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<VirtualTicketsX> virtualProdList = pageInfo.getList();
        virtualProdList.forEach(VirtualTicketsX ->{
            VirtualProd virtualProd = new VirtualProd();
            VirtualProdX virtualProdX = prodDao.findByVirtualProdMpu(VirtualTicketsX.getMpu());
            virtualProd.setEffectiveDays(virtualProdX.getEffectiveDays());
            virtualProd.setId(virtualProdX.getId());
            virtualProd.setMpu(virtualProdX.getMpu());
            virtualProd.setParValue(virtualProdX.getParValue());
            VirtualTicketsX.setVirtualProd(virtualProd);
        });
        pageableData.setList(virtualProdList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public VirtualTickets findVirtualTicketById(int virtualId) {
        return ticketsDao.findVirtualTicketById(virtualId);
    }

    @Override
    public int ticketsInvalid(int virtualId) {
        return ticketsDao.ticketsInvalid(virtualId);
    }

    @Override
    public VirtualTicketsX findByVirtualProdcode(String code) {
        VirtualTicketsX virtualTickets = ticketsDao.findByVirtualTicketscode(code);
        if(virtualTickets != null){
            VirtualProd virtualProd = new VirtualProd();
            VirtualProdX virtualProdX = prodDao.findByVirtualProdMpu(virtualTickets.getMpu());
            virtualProd.setEffectiveDays(virtualProdX.getEffectiveDays());
            virtualProd.setId(virtualProdX.getId());
            virtualProd.setMpu(virtualProdX.getMpu());
            virtualProd.setParValue(virtualProdX.getParValue());
            virtualTickets.setVirtualProd(virtualProd);
        }
        return virtualTickets;
    }
}
