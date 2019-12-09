package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PromotionTypeResDto;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PromotionTypeServiceImpl implements PromotionTypeService {

    @Autowired
    private PromotionTypeDao promotionTypeDao;

    @Override
    public PageableData<PromotionType> getPromotionTypes(int page, int size, String appId) {

        PageableData<PromotionType> pageableData = new PageableData<>();
        log.info("getPromotionTypes page, size==> " , page, size);
        // 执行查询
        PageInfo<PromotionType> pageInfo =  promotionTypeDao.selectPromotionType(page, size, appId);

        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<PromotionType> groupInfoList = pageInfo.getList();
        pageableData.setList(groupInfoList);
        pageableData.setPageInfo(pageVo);

        log.info("分页查询活动列表 得到PageableData为:{}", JSONUtil.toJsonString(pageableData));

        return pageableData;
    }

    @Override
    public List<PromotionTypeResDto> queryAllPromotionType() {
        List<PromotionType> promotionTypeList = promotionTypeDao.selectAllPromotionTypeList();
        log.info("查询所有活动类型列表 数据库返回:{}", JSONUtil.toJsonString(promotionTypeList));

        // 转dto
        List<PromotionTypeResDto> promotionTypeResDtoList = new ArrayList<>();
        for (PromotionType promotionType : promotionTypeList) {
            PromotionTypeResDto promotionTypeResDto = convertToPromotionTypeResDto(promotionType);

            promotionTypeResDtoList.add(promotionTypeResDto);
        }

        log.info("查询所有活动类型列表 PromotionTypeServiceImpl#queryAllPromotionType 返回:{}",
                JSONUtil.toJsonString(promotionTypeList));

        return promotionTypeResDtoList;
    }

    @Override
    public Long createPromotionType(PromotionType type) {
        return promotionTypeDao.createPromotionType(type);
    }

    @Override
    public Long updatePromotionType(PromotionType type) {
        return promotionTypeDao.updatePromotionType(type);
    }

    @Override
    public int removePromotionType(Long promotionTypeId) {
        return promotionTypeDao.removePromotionType(promotionTypeId);
    }

    // ============================== private ===========================================

    private PromotionTypeResDto convertToPromotionTypeResDto(PromotionType promotionType) {
        PromotionTypeResDto promotionTypeResDto = new PromotionTypeResDto();

        promotionTypeResDto.setId(promotionType.getId());
        promotionTypeResDto.setTypeName(promotionType.getTypeName());
        promotionTypeResDto.setTypeDesc(promotionType.getTypeDesc());
        promotionTypeResDto.setIstatus(promotionType.getIstatus());
        promotionTypeResDto.setCreateTime(promotionType.getCreateTime());
        promotionTypeResDto.setUpdateTime(promotionType.getUpdateTime());

        return promotionTypeResDto;
    }
}
