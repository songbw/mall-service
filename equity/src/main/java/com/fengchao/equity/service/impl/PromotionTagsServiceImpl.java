package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.PromotionTagsDao;
import com.fengchao.equity.model.PromotionTags;
import com.fengchao.equity.service.PromotionTagsService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromotionTagsServiceImpl implements PromotionTagsService {

    @Autowired
    private PromotionTagsDao tagsDao;

    @Override
    public int createPromotionTags(PromotionTags bean) {
        return tagsDao.createPromotionTags(bean);
    }

    @Override
    public PageableData<PromotionTags> findPromotionTags(Integer pageNo, Integer pageSize, String appId) {
        PageableData<PromotionTags> pageableData = new PageableData<>();
        PageInfo<PromotionTags> pageInfo = tagsDao.findPromotionTags(pageNo, pageSize, appId);

        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<PromotionTags> groupInfoList = pageInfo.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);
        return pageableData;
    }

    @Override
    public int updatePromotionTags(PromotionTags bean) {
        return tagsDao.updatePromotionTags(bean);
    }

    @Override
    public int deletePromotionTags(Integer id) {
        return tagsDao.deletePromotionTags(id);
    }
}
