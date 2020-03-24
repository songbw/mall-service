package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.CardDetailsBean;
import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.dao.CardInfoDao;
import com.fengchao.equity.dao.CouponUseInfoDao;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CardInfoService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardInfoServiceImpl implements CardInfoService {

    @Autowired
    private CardInfoDao dao;
    @Autowired
    private CardTicketDao assignsDao;
    @Autowired
    private CardAndCouponDao cardAndCouponDao;
    @Autowired
    private CouponUseInfoDao couponUseInfoDao;

    @Override
    public int createCardInfo(CardInfoBean bean) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setAmount(bean.getAmount());
        cardInfo.setName(bean.getName());
        cardInfo.setEffectiveDays(bean.getEffectiveDays());
        cardInfo.setType(bean.getType());
        cardInfo.setAppId(bean.getAppId());
        cardInfo.setCorporationCode(bean.getCorporationCode());
        int cardTicket = dao.createCardTicket(cardInfo);
        if(cardTicket == 1){
            for (int i = 0; i < bean.getCouponIds().size(); i++){
                CardAndCoupon cardAndCoupon = new CardAndCoupon();
                cardAndCoupon.setCardId(cardInfo.getId());
                cardAndCoupon.setCouponId(bean.getCouponIds().get(i));
                cardAndCouponDao.create(cardAndCoupon);
            }
        }
        return cardTicket;
    }

    @Override
    public int updateCardInfo(CardInfo bean) {
        return dao.updateCardTicket(bean);
    }

    @Override
    public CardInfoX findByCardId(Integer id) {
        CardInfo cardInfo = dao.findByCardId(id);
        List<CardAndCoupon> couponIds= cardAndCouponDao.findCouponIdByCardId(id);
        CardInfoX cardInfoX = new CardInfoX();
        BeanUtils.copyProperties(cardInfo,cardInfoX);
        cardInfoX.setCouponIds(couponIds);

        return cardInfoX;
    }

    @Override
    public PageableData<CardInfoX> findCardInfo(CardInfoBean bean) {
        PageableData<CardInfoX> pageableData = new PageableData<>();

        PageInfo<CardInfo> cardTicket = dao.findCardTicket(bean);
        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(cardTicket);
        List<CardInfo> cardTicketList = cardTicket.getList();
        List<CardInfoX> infoXList = new ArrayList<>();
        for(CardInfo info: cardTicketList){
            CardInfoX cardInfoX = new CardInfoX();
            BeanUtils.copyProperties(info,cardInfoX);
            List<CardAndCoupon> couponIds= cardAndCouponDao.findCouponIdByCardId(info.getId());
            cardInfoX.setCouponIds(couponIds);
            infoXList.add(cardInfoX);
        }
        pageableData.setList(infoXList);
        pageableData.setPageInfo(pageVo);

        return pageableData;
    }

    @Override
    public int deleteCardInfo(Integer id) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setId(id);
        cardInfo.setIsDelete((short) 2);
        int num = dao.updateCardTicket(cardInfo);
        if(num == 1){
            CardAndCoupon cardAndCoupon = new CardAndCoupon();
            cardAndCoupon.setIsDelete((short) 2);
            cardAndCoupon.setCardId(id);
            cardAndCouponDao.updateCardAndCoupon(cardAndCoupon);
        }
        return dao.updateCardTicket(cardInfo);
    }

    @Override
    public PageableData<CardDetailsBean> details(CardInfoBean bean) {
        PageableData<CardDetailsBean> pageableData = new PageableData<>();
        PageInfo<CardTicket> cardTicketPageInfo = assignsDao.searchCardTicket(bean);
        PageVo pageVo = ConvertUtil.convertToPageVo(cardTicketPageInfo);
        pageableData.setPageInfo(pageVo);

        List<CardTicket> cardTicketList = cardTicketPageInfo.getList();
        List<String> userCouponCodeList = new ArrayList<>();
        for(CardTicket ticket: cardTicketList){
            userCouponCodeList.add(ticket.getUserCouponCode());
        }
        List<CouponUseInfo> couponUseInfos = couponUseInfoDao.selectByUserCouponCodeList(userCouponCodeList);
        Map<String,String> codeOrderMap = new HashMap<>();
        if (null != couponUseInfos && 0 < couponUseInfos.size()) {
            for (CouponUseInfo info : couponUseInfos) {
                codeOrderMap.put(info.getUserCouponCode(), info.getOrderId());
            }
        }
        List<CardDetailsBean> beans = new ArrayList<>();
        for(CardTicket ticket: cardTicketList){
            CardDetailsBean detailsBean = new CardDetailsBean();
            BeanUtils.copyProperties(ticket,detailsBean);
            if (null != codeOrderMap) {
                detailsBean.setOrderId(codeOrderMap.get(ticket.getUserCouponCode()));
            }
            beans.add(detailsBean);
        }

        pageableData.setList(beans);

        return pageableData;
    }
}
