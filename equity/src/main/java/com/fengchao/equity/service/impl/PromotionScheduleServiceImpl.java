package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PromotionScheduleBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.PromotionScheduleDao;
import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.service.PromotionScheduleService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PromotionScheduleServiceImpl implements PromotionScheduleService {

    @Autowired
    private PromotionScheduleDao scheduleDao;

    @Override
    public int createSchedule(PromotionScheduleBean bean) {
//        if(bean.getPromotionId() == null){
//            return 500;
//        }
        PromotionSchedule promotionSchedule = new PromotionSchedule();
        promotionSchedule.setCreateTime(new Date());
        promotionSchedule.setSchedule(bean.getSchedule());
        promotionSchedule.setPromotionId(bean.getPromotionId());
        promotionSchedule.setStartTime(bean.getStartTime());
        promotionSchedule.setEndTime(bean.getEndTime());
        return scheduleDao.createPromotionSchedule(promotionSchedule);
    }

    @Override
    public PageableData<PromotionSchedule> findSchedule(Integer offset, Integer limit) {
        PageableData<PromotionSchedule> pageableData = new PageableData<>();
        PageInfo<PromotionSchedule> pageInfo = scheduleDao.findSchedule(offset, limit);
        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<PromotionSchedule> groupInfoList = pageInfo.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

//    @Override
//    public PromotionSchedule findScheduleById(Integer id) {
//        return scheduleDao.findPromotionSchedule(id);
//    }

    @Override
    public int deletePromotion(Integer id) {
        return scheduleDao.removePromotionSchedule(id);
    }

    @Override
    public int updateSchedule(PromotionScheduleBean bean) {
        PromotionSchedule promotionSchedule = new PromotionSchedule();
        promotionSchedule.setId(bean.getId());
        promotionSchedule.setUpdateTime(new Date());
        promotionSchedule.setSchedule(bean.getSchedule());
        promotionSchedule.setPromotionId(bean.getPromotionId());
        promotionSchedule.setStartTime(bean.getStartTime());
        promotionSchedule.setEndTime(bean.getEndTime());
        return scheduleDao.updatePromotionSchedule(promotionSchedule);
    }
}
