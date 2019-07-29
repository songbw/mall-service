package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.PromotionTypeDao;
import com.fengchao.equity.model.PromotionType;
import com.fengchao.equity.service.PromotionTypeService;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.JSONUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromotionTypeServiceImpl implements PromotionTypeService {

    @Autowired
    private PromotionTypeDao promotionTypeDao;

    @Override
    public PageableData<PromotionType> getPromotionTypes(int page, int size) {

        PageableData<PromotionType> pageableData = new PageableData<>();

        // 执行查询
        PageInfo<PromotionType> pageInfo =  promotionTypeDao.selectPromotionType(page, size);

        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<PromotionType> groupInfoList = pageInfo.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);

        log.info("分页查询活动列表 得到PageableData为:{}", JSONUtil.toJsonString(pageableData));

        return pageableData;

    }
}
