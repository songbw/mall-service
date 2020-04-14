package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.CardDetailsBean;
import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.CardInfoOfCorporationBean;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.CardAndCouponDao;
import com.fengchao.equity.dao.CardTicketDao;
import com.fengchao.equity.dao.CardInfoDao;
import com.fengchao.equity.dao.CouponUseInfoDao;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.feign.IWelfareClient;
import com.fengchao.equity.model.*;
import com.fengchao.equity.service.CardInfoService;
import com.fengchao.equity.utils.CardInfoStatusEnum;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.MyErrorEnum;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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
    @Autowired
    private IWelfareClient welfareClient;

    @Override
    public CardInfo createCardInfo(CardInfoBean bean) {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setAmount(bean.getAmount());
        cardInfo.setName(bean.getName());
        cardInfo.setEffectiveDays(bean.getEffectiveDays());
        cardInfo.setType(bean.getType());
        cardInfo.setAppId(bean.getAppId());
        cardInfo.setCorporationCode(bean.getCorporationCode());
        cardInfo.setCode(bean.getCode());
        int index = dao.createCardTicket(cardInfo);
        if(0 < index && null != bean.getCouponIds() && 0 < bean.getCouponIds().size()){
            for (int i = 0; i < bean.getCouponIds().size(); i++){
                CardAndCoupon cardAndCoupon = new CardAndCoupon();
                cardAndCoupon.setCardId(cardInfo.getId());
                cardAndCoupon.setCouponId(bean.getCouponIds().get(i));
                cardAndCouponDao.create(cardAndCoupon);
            }
        }

        return cardInfo;
    }

    @Override
    public int updateCardInfo(CardInfo bean) {
        Integer id = bean.getId();
        if (null == id || 0 == id){
            throw new EquityException(MyErrorEnum.CARD_INFO_ID_BLANK);
        }
        CardInfo record = dao.findById(id);
        if(null == record){
            throw new EquityException(MyErrorEnum.CARD_INFO_NOT_FOUND);
        }

        Short updateStatus = bean.getStatus();
        if (null != updateStatus){
            Integer status = (int)record.getStatus();
            if (CardInfoStatusEnum.INVALID.getCode().equals(status)) {
                if(!CardInfoStatusEnum.RELEASED.getCode().equals(updateStatus)) {
                    throw new EquityException(MyErrorEnum.CARD_INFO_WAS_INVALID);
                }
            }else if (CardInfoStatusEnum.RELEASED.getCode().equals(status)) {
                if(CardInfoStatusEnum.EDITING.getCode().equals(updateStatus)) {
                    throw new EquityException(MyErrorEnum.CARD_INFO_HAS_RELEASE);
                }
                log.info("已经发布的提货券只能更改状态,当前记录为： {}", JSON.toJSONString(record));
                CardInfo updateBean = new CardInfo();
                updateBean.setId(bean.getId());
                updateBean.setStatus(bean.getStatus());
                return dao.updateCardTicket(updateBean);
            }

        }

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

    @Override
    public List<CardInfoOfCorporationBean>
    getByCorporation(String corporationCode){

        log.info("服务调用福利模块,获取cardInfoCode列表, corporationCode={}",corporationCode);
        JSONObject remoteResult = welfareClient.getCardInfoCodes(corporationCode);
        if(null == remoteResult){
            throw new EquityException(MyErrorEnum.WELFARE_NOT_GET_CARD_INFO_CODE_LIST_FAILED);
        }
        log.info("服务调用福利模块,获取cardInfoCode列表 返回： {}",JSON.toJSONString(remoteResult));
        Integer code = remoteResult.getInteger("code");
        if(null == code || 200 != code){
            throw new EquityException(MyErrorEnum.WELFARE_NOT_GET_CARD_INFO_CODE_LIST_FAILED);
        }
        Object data = remoteResult.get("data");
        if (null == data){
            throw new EquityException(MyErrorEnum.WELFARE_NOT_GET_CARD_INFO_CODE_LIST_FAILED);
        }

        List<String> list = JSON.parseArray(JSON.toJSONString(data), String.class);
        if(null == list || 0 == list.size()){
            return new ArrayList<>();
        }

        List<CardInfo> cardInfoList = dao.findByCodeList(list);
        List<CardInfoOfCorporationBean> result = new ArrayList<>();
        Map<String, List<CardInfo>> map = cardInfoList.stream().collect(Collectors.groupingBy(CardInfo::getCorporationCode));
        for (Map.Entry<String, List<CardInfo>> infos : map.entrySet()) {
            CardInfoOfCorporationBean bean = new CardInfoOfCorporationBean();
            bean.setCorporationCode(infos.getKey());
            bean.setCardInfoList(infos.getValue());
            result.add(bean);
        }

        return result;
    }
}
