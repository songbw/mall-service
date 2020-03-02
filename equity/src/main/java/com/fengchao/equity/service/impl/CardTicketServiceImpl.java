package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.dao.*;
import com.fengchao.equity.model.*;
import com.fengchao.equity.rpc.ProductRpcService;
import com.fengchao.equity.service.CardTicketService;
import com.fengchao.equity.utils.CardTicketStatusEnum;
import com.fengchao.equity.utils.DataUtils;
import com.fengchao.equity.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Service
public class CardTicketServiceImpl implements CardTicketService {

    @Autowired
    private CardInfoDao infoDao;
    @Autowired
    private CardTicketDao ticketDao;
    @Autowired
    private CardAndCouponDao cardAndCouponDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponUseInfoDao useInfoDao;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private Environment environment;
    @Autowired
    private ProductRpcService prodService;

    @Override
    public int assignsCardTicket(CardTicketBean bean) {
        List<CardTicket> tickets = new ArrayList<>();
        for (int i = 0; i < bean.getNum(); i++){
            CardTicket ticket = new CardTicket();
            String code = String.valueOf(System.currentTimeMillis()) + (int)((Math.random()*9+1)*100000);
            ticket.setCardId(bean.getCardId());
            ticket.setCard(code);
            ticket.setPassword(String.valueOf((int)((Math.random()*9+1)*100000)));
            ticket.setRemark(bean.getRemark());
            tickets.add(ticket);
        }
        return ticketDao.insertBatch(tickets);
    }

    @Override
    public List<String> activatesCardTicket(List<CardTicket> beans) {
        List<String> resultCards = new ArrayList();
        List<String> cards = new ArrayList();
        for (CardTicket ticket: beans){
            cards.add(ticket.getCard());
        }

        List<CardTicket> activateTicket = ticketDao.findActivateTicket(cards);
        if(!activateTicket.isEmpty()){
            for (CardTicket ticket: activateTicket){
                resultCards.add(ticket.getCard());
            }
        }

        Date date = new Date();
        for(CardTicket ticket: beans){
            CardTicketX cardTicketX = ticketDao.findbyCard(ticket.getCard());
            CardInfoX cardInfoX = infoDao.findByCardId(cardTicketX.getCardId());
            Date fetureDate = DataUtils.getFetureDate(date, cardInfoX.getEffectiveDays());
            ticket.setEndTime(fetureDate);
            JobClientUtils.cardInvalidTrigger(environment, jobClient, ticket.getId(), fetureDate);
        }
        ticketDao.activatesCardTicket(beans);
        return resultCards;
    }

    @Override
    public List<CardInfoX> exportCardTicket(ExportCardBean bean) {

        List<CardInfoX> infoXES = infoDao.findByIds(bean);
        for (CardInfoX infoX: infoXES){
            List<CardAndCoupon> couponIds= cardAndCouponDao.findCouponIdByCardId(infoX.getId());
            infoX.setCouponIds(couponIds);
            List<CardTicket> tickets = ticketDao.findbyCardId(infoX.getId(), bean.getStatus());
            infoX.setTickets(tickets);
        }
        return infoXES;
    }

    @Override
    public int verifyCardTicket(CardTicketBean bean) throws Exception{
        List<CardTicket> tickets = ticketDao.selectCardTicketByCard(bean);
        if(tickets.isEmpty()){
            throw new Exception("账号或密码错误");
        }

        CardTicket cardTicket = tickets.get(0);
        if(StringUtils.isNotEmpty(cardTicket.getOpenId())){
            throw new Exception("该卡已被绑定");
        }
        CardInfo cardInfo;
        try {
            cardInfo = infoDao.findById(cardTicket.getCardId());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new Exception("内部错误,未找到提货券信息");
        }
        if (null == cardInfo || null == cardInfo.getAppId()){
            throw new Exception("内部错误,未找到提货券或提货券渠道信息");
        }
        if (!cardInfo.getAppId().equals(bean.getAppId())){
            throw new Exception("内部错误,应用渠道与提货券渠道不符");
        }

        int status = (int)cardTicket.getStatus();
        if (CardTicketStatusEnum.ACTIVE.getCode() != status) {
            throw new Exception("绑卡失败，该卡"+CardTicketStatusEnum.Int2String(status));
        }

        CardTicket ticket = new CardTicket();
        ticket.setOpenId(bean.getOpenId());
        ticket.setId(cardTicket.getId());
        ticket.setStatus((short)CardTicketStatusEnum.BOUND.getCode());

        return ticketDao.update(ticket);
    }

    @Override
    public CouponUseInfo exchangeCardTicket(CardTicketBean bean)  throws Exception{
        if (null == bean.getAppId()){
            throw new Exception("兑换失败,应用缺失渠道信息");
        }
        String userCouponCode = "";
        Coupon coupon = couponDao.selectCouponById(bean.getCouponId());
        CardTicketX cardTicket = ticketDao.findbyCard(bean.getCard());
        CardInfoX cardInfoX = infoDao.findByCardId(cardTicket.getCardId());
        if (!bean.getAppId().equals(cardInfoX.getAppId())){
            throw new Exception("兑换失败,应用渠道与提货券渠道不符");
        }
        if(coupon.getCouponType() == 4 && cardTicket.getStatus() == CardTicketStatusEnum.BOUND.getCode()){
            Date date = new Date();
            DecimalFormat df=new DecimalFormat("0000");
            CouponUseInfo couponUseInfo = new CouponUseInfo();
            couponUseInfo.setCollectedTime(date);
            couponUseInfo.setCouponId(coupon.getId());
            couponUseInfo.setEffectiveStartDate(date);
            Date fetureDate = DataUtils.getFetureDate(date, cardInfoX.getEffectiveDays());
            couponUseInfo.setEffectiveEndDate(fetureDate);
            couponUseInfo.setCode(coupon.getCode());
            couponUseInfo.setUserOpenId(cardTicket.getOpenId());
            userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + System.currentTimeMillis() + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setUserCouponCode(userCouponCode);
            couponUseInfo.setAppId(coupon.getAppId());
            int insert = useInfoDao.insert(couponUseInfo);
            if(insert == 1){
                JobClientUtils.couponUseInfoInvalidTrigger(environment, jobClient, couponUseInfo.getId(), fetureDate);
                CardTicket ticket = new CardTicket();
                ticket.setStatus((short)4);
                ticket.setId(cardTicket.getId());
                ticket.setUserCouponCode(userCouponCode);
                ticketDao.update(ticket);
                return couponUseInfo;
            }else{
                throw new Exception("兑换失败");
            }
        }else{
            throw new Exception("兑换失败");
        }
    }

    @Override
    public List<CardTicketX> getCardTicket(String openId) {
        List<CardTicketX> tickets = ticketDao.seleteCardTicketByOpenId(openId);
        for(CardTicketX ticket: tickets){
            CardInfo infoX = infoDao.findById(ticket.getCardId());
            ticket.setCardInfo(infoX);

            List<CouponBean> couponBeanList = new ArrayList();
            if(StringUtils.isNotEmpty(ticket.getUserCouponCode())){
                ArrayList<CouponUseInfoX> useInfoXList = new ArrayList<>();
                CouponUseInfoX userCouponCode = useInfoDao.findByUserCouponCode(ticket.getUserCouponCode());
                if(userCouponCode != null){
                    CouponX coupon = couponDao.selectCouponXById(userCouponCode.getCouponId());
                    CouponBean couponBean = couponToBean(coupon);
                    couponBean.setCouponUseInfo(useInfoXList);
                    couponBeanList.add(couponBean);
                }
                useInfoXList.add(userCouponCode);
            }else{
                List<CardAndCoupon> cardAndCoupons = cardAndCouponDao.findCouponIdByCardId(ticket.getCardId());
                for (CardAndCoupon cardAndCoupon: cardAndCoupons){
                    CouponX couponX = couponDao.selectCouponXById(cardAndCoupon.getCouponId());
                    if(couponX != null){
                        CouponBean couponBean = couponToBean(couponX);
                        couponBeanList.add(couponBean);
                    }
                }
            }
            ticket.setCoupons(couponBeanList);
        }
        return tickets;
    }

    @Override
    public CardTicket findById(int id) {
        return ticketDao.findById(id);
    }

    @Override
    public int invalid(int id) {
        CardTicket ticket = new CardTicket();
        ticket.setId(id);
        ticket.setStatus((short)CardTicketStatusEnum.TIMEOUT.getCode());
        return ticketDao.update(ticket);
    }

    @Override
    public Map<String, String> selectPlatformAll() {
        Map<String, String> platformMap = new HashMap<String, String>();
        List<Platform> platformAll = prodService.findPlatformAll();
        for (Platform platform : platformAll) {
            if (platform != null) {
                platformMap.put(platform.getAppId(), platform.getName());
            }
        }
        return platformMap;
    }

    @Override
    public CardTicketX getCardTicketByCard(String openId, String card) {
        CardTicketX ticket = ticketDao.seleteCardTicketByCard(openId, card);
        if(ticket != null){
            CardInfo infoX = infoDao.findById(ticket.getCardId());
            ticket.setCardInfo(infoX);

            List<CouponBean> couponBeanList = new ArrayList();
            if(StringUtils.isNotEmpty(ticket.getUserCouponCode())){
                ArrayList<CouponUseInfoX> useInfoXList = new ArrayList<>();
                CouponUseInfoX userCouponCode = useInfoDao.findByUserCouponCode(ticket.getUserCouponCode());
                if(userCouponCode != null){
                    CouponX coupon = couponDao.selectCouponXById(userCouponCode.getCouponId());
                    CouponBean couponBean = couponToBean(coupon);
                    couponBean.setCouponUseInfo(useInfoXList);
                    couponBeanList.add(couponBean);
                }
                useInfoXList.add(userCouponCode);
            }else{
                List<CardAndCoupon> cardAndCoupons = cardAndCouponDao.findCouponIdByCardId(ticket.getCardId());
                for (CardAndCoupon cardAndCoupon: cardAndCoupons){
                    CouponX couponX = couponDao.selectCouponXById(cardAndCoupon.getCouponId());
                    if(couponX != null){
                        CouponBean couponBean = couponToBean(couponX);
                        couponBeanList.add(couponBean);
                    }
                }
            }
            ticket.setCoupons(couponBeanList);
        }
        return ticket;
    }

    @Override
    public int deleteCardTicket(Integer id) {
        return ticketDao.deleteCardTicket(id);
    }

    private CouponBean couponToBean(CouponX coupon){

        CouponBean couponBean = new CouponBean();

        if(coupon == null){
            return couponBean;
        }
        couponBean.setId(coupon.getId());
        couponBean.setName(coupon.getName());
        couponBean.setSupplierMerchantId(coupon.getSupplierMerchantId());
        couponBean.setSupplierMerchantName(coupon.getSupplierMerchantName());
        couponBean.setReleaseTotal(coupon.getReleaseTotal());
        couponBean.setReleaseNum(coupon.getReleaseNum());
        couponBean.setReleaseStartDate(coupon.getReleaseStartDate());
        couponBean.setReleaseEndDate(coupon.getReleaseEndDate());
        couponBean.setStatus(coupon.getStatus());
        couponBean.setEffectiveStartDate(coupon.getEffectiveStartDate());
        couponBean.setEffectiveEndDate(coupon.getEffectiveEndDate());
        couponBean.setDescription(coupon.getDescription());
        couponBean.setEffectiveDays(coupon.getEffectiveDays());
        couponBean.setUserCollectNum(coupon.getUserCollectNum());
        if(coupon.getExcludeDates()!= null && !"".equals(coupon.getExcludeDates())) {
            couponBean.setExcludeDates(JSONArray.parseArray(coupon.getExcludeDates()));
        }
        couponBean.setUrl(coupon.getUrl());
        couponBean.setCreateDate(coupon.getCreateDate());
        couponBean.setCategory(coupon.getCategory());
        if(coupon.getTags() != null && !"".equals(coupon.getTags())){
            String[] tagsStr = coupon.getTags().split(", ");
            int[] tagsNum = new int[tagsStr.length];
            for (int i = 0; i < tagsStr.length; i++) {
                tagsNum[i] = Integer.parseInt(tagsStr[i]);
            }
            couponBean.setTags(tagsNum);
        }
        couponBean.setImageUrl(coupon.getImageUrl());
        couponBean.setAppId(coupon.getAppId());
        Rules rules = new Rules();
        Scenario scenario = new Scenario();
        rules.setScenario(scenario);
        Collect collect = new Collect();
        rules.setCollect(collect);
        Customer customer = new Customer();
        rules.setCustomer(customer);
        couponBean.setRules(rules);
        if(couponBean.getRules() != null){
            couponBean.getRules().setCode(coupon.getCode());
            couponBean.getRules().setRulesDescription(coupon.getRulesDescription());
            couponBean.getRules().setPerLimited(coupon.getPerLimited());
            if(coupon.getScopes() != null){
                couponBean.getRules().setScopes(coupon.getScopes().split(","));
            }
            couponBean.getRules().getScenario().setType(coupon.getScenarioType());
            if(coupon.getCouponMpus() != null){
                couponBean.getRules().getScenario().setCouponMpus(coupon.getCouponMpus().split(","));
            }
            if(coupon.getExcludeMpus() != null){
                couponBean.getRules().getScenario().setExcludeMpus(coupon.getExcludeMpus().split(","));
            }
            if(coupon.getCategories() != null){
                couponBean.getRules().getScenario().setCategories(coupon.getCategories().split(","));
            }
            if(coupon.getBrands() != null){
                couponBean.getRules().getScenario().setBrands(coupon.getBrands().split(","));
            }
            couponBean.getRules().getCollect().setType(coupon.getCollectType());
            if(coupon.getPoints() != null){
                couponBean.getRules().getCollect().setPoints(coupon.getPoints());
            }
            couponBean.getRules().getCustomer().setType(coupon.getCustomerType());
            if(coupon.getUsers() != null){
                couponBean.getRules().getCustomer().setUsers(coupon.getUsers().split(","));
            }
            if(coupon.getCouponRules()!= null && !"".equals(coupon.getCouponRules())) {
                couponBean.getRules().setCouponRules(JSONArray.parseObject(coupon.getCouponRules()));
            }
        }

        return couponBean;
    }
}
