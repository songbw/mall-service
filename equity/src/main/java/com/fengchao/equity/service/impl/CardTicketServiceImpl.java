package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.*;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.feign.VendorsService;
import com.fengchao.equity.mapper.CouponXMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.rpc.ProductRpcService;
import com.fengchao.equity.service.CardTicketService;
import com.fengchao.equity.utils.*;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author fengchao
 * */

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
    private CouponXMapper couponXMapper;
    @Autowired
    private CouponUseInfoDao useInfoDao;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private Environment environment;
    @Autowired
    private ProductRpcService prodService;
    @Autowired
    private VendorsService vendorsService;

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
            ticket.setCorporationCode(bean.getCorporationCode());
            ticket.setCardInfoCode(bean.getCardInfoCode());
            ticket.setWelfareOrderNo(bean.getWelfareOrderNo());
            tickets.add(ticket);
        }
        return ticketDao.insertBatch(tickets);
    }

    @Override
    public List<String> activatesCardTicket(List<CardTicket> beans) {
        List<String> resultCards = new ArrayList<>();
        List<String> cards = new ArrayList<>();
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
            CardTicket cardTicket = ticketDao.findByCard(ticket.getCard());
            CardInfo cardInfo = infoDao.findByCardId(cardTicket.getCardId());
            Date fetureDate = DataUtils.getFetureDate(date, cardInfo.getEffectiveDays());
            ticket.setEndTime(fetureDate);
            JobClientUtils.cardInvalidTrigger(environment, jobClient, ticket.getCard(), fetureDate);
        }
        ticketDao.activatesCardTicket(beans);
        return resultCards;
    }

    @Override
    public List<String>
    batchCreateActiveCardTickets(CardTicketBean bean){

        List<String> cardCodeList = new ArrayList<>();
        Date date = new Date();
        CardInfo cardInfo = infoDao.findByCardCode(bean.getCardInfoCode());
        Date futureDate = DataUtils.getFetureDate(date, cardInfo.getEffectiveDays());

        List<CardTicket> tickets = new ArrayList<>();
        for (int i = 0; i < bean.getNum(); i++){
            CardTicket ticket = new CardTicket();
            String code = String.valueOf(System.currentTimeMillis()) + (int)((Math.random()*9+1)*100000);
            cardCodeList.add(code);
            ticket.setCardId(cardInfo.getId());
            ticket.setCard(code);
            ticket.setPassword(String.valueOf((int)((Math.random()*9+1)*100000)));
            ticket.setRemark(bean.getRemark());
            ticket.setCorporationCode(bean.getCorporationCode());
            ticket.setCardInfoCode(bean.getCardInfoCode());
            ticket.setWelfareOrderNo(bean.getWelfareOrderNo());
            ticket.setEndTime(futureDate);
            tickets.add(ticket);
            JobClientUtils.cardInvalidTrigger(environment, jobClient, ticket.getCard(), futureDate);

        }
        ticketDao.batchInsertActiveCardTickets(tickets);

        return cardCodeList;

    }

    @Override
    public List<CardTicket>
    batchGetTicketsByCodeList(List<String> codeList){
       return ticketDao.batchFindByCardCodeList(codeList,null);
    }

    @Override
    public List<TicketToEmployeeBean>
    batchAssignTicketsByPhone(List<TicketToEmployeeBean> list){

        List<TicketToEmployeeBean> unAssignedList = new ArrayList<>();
        List<String> phoneList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        Map<String,String> codePhoneMap = new HashMap<>();
        for(TicketToEmployeeBean bean: list){
            phoneList.add(bean.getPhone());
            codeList.add(bean.getCard());
            codePhoneMap.put(bean.getCard(),bean.getPhone());
        }

        Map<String,Object> map = getEmployeeCodeMapByPhoneList(phoneList);
        List<CardTicket> tickets = ticketDao.batchFindByCardCodeList(codeList,CardTicketStatusEnum.ACTIVE);

        if(null != tickets && null != map && 0 < tickets.size() && 0 < map.size()){
            for(CardTicket ticket: tickets){
                ticket.setEmployeeCode(map.get(codePhoneMap.get(ticket.getCard())).toString());
                ticket.setStatus((short)CardTicketStatusEnum.ASSIGNED.getCode());
            }

            ticketDao.batchAssignCardTicket(tickets);

        }
        List<CardTicket> unAssignedTickets = ticketDao.batchFindByCardCodeList(codeList,CardTicketStatusEnum.ACTIVE);
        for(CardTicket ticket: unAssignedTickets) {
            TicketToEmployeeBean faultBean = new TicketToEmployeeBean();
            faultBean.setCard(ticket.getCard());
            faultBean.setPhone(codePhoneMap.get(ticket.getCard()));
            unAssignedList.add(faultBean);

        }

        return unAssignedList;
    }

    @Override
    public List<CardInfoX> exportCardTicket(ExportCardBean bean) {

        List<CardInfoX> result = new ArrayList<>();
        List<CardInfo> list = infoDao.findByIds(bean);
        for (CardInfo info: list){
            CardInfoX infoX = new CardInfoX();
            BeanUtils.copyProperties(info,infoX);
            List<CardAndCoupon> couponIds= cardAndCouponDao.findCouponIdByCardId(infoX.getId());
            infoX.setCouponIds(couponIds);
            List<CardTicket> tickets = ticketDao.findbyCardId(infoX.getId(), bean.getStatus());
            infoX.setTickets(tickets);
            result.add(infoX);
        }
        return result;
    }

    @Override
    public int verifyCardTicket(CardTicketBean bean) {
        List<CardTicket> tickets = ticketDao.selectCardTicketByCard(bean);
        if(tickets.isEmpty()){
            throw new EquityException(MyErrorEnum.ACCOUNT_PASSWORD_WRONG);
        }

        CardTicket cardTicket = tickets.get(0);
        if(StringUtils.isNotEmpty(cardTicket.getOpenId())){
            throw new EquityException(MyErrorEnum.CARD_TICKET_STATUS_EXCHANGED);
        }
        CardInfo cardInfo;
        try {
            cardInfo = infoDao.findById(cardTicket.getCardId());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EquityException(MyErrorEnum.CARD_TICKET_MISSING);
        }
        if (null == cardInfo || null == cardInfo.getAppId()){
            throw new EquityException(MyErrorEnum.CARD_TICKET_MISSING);
        }
        if (!cardInfo.getAppId().equals(bean.getAppId())){
            throw new EquityException(MyErrorEnum.CARD_INFO_APP_ID_NOT_MATCH);
        }

        int status = (int)cardTicket.getStatus();
        if (CardTicketStatusEnum.ACTIVE.getCode() != status) {
            throw new EquityException(MyErrorEnum.CARD_TICKET_BIND_EXCHANGE_FAILED.getCode(),
                    MyErrorEnum.CARD_TICKET_BIND_EXCHANGE_FAILED.getMsg()+CardTicketStatusEnum.int2msg(status));
        }

        CardTicket ticket = new CardTicket();
        ticket.setOpenId(bean.getOpenId());
        ticket.setId(cardTicket.getId());
        ticket.setStatus((short)CardTicketStatusEnum.BOUND.getCode());

        return ticketDao.update(ticket);
    }

    @Override
    public CouponUseInfo exchangeCardTicket(CardTicketBean bean) {
        if (null == bean.getAppId()){
            throw new EquityException(MyErrorEnum.CARD_TICKET_HEADER_APP_ID_BLANK);
        }

        String userCouponCode = "";

        Coupon coupon = couponDao.selectCouponById(bean.getCouponId());
        if (null == coupon){
            throw new EquityException(MyErrorEnum.COUPON_NOT_FOUND);
        }
        CardTicket cardTicket = ticketDao.findByCard(bean.getCard());
        if (null == cardTicket || null == cardTicket.getId()){
            throw new EquityException(MyErrorEnum.CARD_TICKET_MISSING);
        }
        CardInfo cardInfo = infoDao.findByCardId(cardTicket.getCardId());
        if (null == cardInfo){
            throw new EquityException(MyErrorEnum.CARD_TICKET_MISSING);
        }
        if (!bean.getAppId().equals(cardInfo.getAppId())){
            throw new EquityException(MyErrorEnum.CARD_INFO_APP_ID_NOT_MATCH);
        }
        if((!CouponTypeEnum.needTrigger(coupon.getCouponType())) &&
                cardTicket.getStatus() == CardTicketStatusEnum.BOUND.getCode()){
            Date date = new Date();
            DecimalFormat df=new DecimalFormat("0000");
            CouponUseInfo couponUseInfo = new CouponUseInfo();
            couponUseInfo.setCollectedTime(date);
            couponUseInfo.setCouponId(coupon.getId());
            couponUseInfo.setEffectiveStartDate(date);
            //Date fetureDate = DataUtils.getFetureDate(date, cardInfo.getEffectiveDays());
            Date fetureDate = cardTicket.getEndTime();
            couponUseInfo.setEffectiveEndDate(fetureDate);
            couponUseInfo.setCode(coupon.getCode());
            couponUseInfo.setUserOpenId(cardTicket.getOpenId());
            userCouponCode = df.format(Integer.parseInt(coupon.getSupplierMerchantId())) + System.currentTimeMillis() + (int)((Math.random()*9+1)*100000);
            couponUseInfo.setUserCouponCode(userCouponCode);
            couponUseInfo.setAppId(coupon.getAppId());
            int insert = useInfoDao.insert(couponUseInfo);
            if(insert == 1){
                couponXMapper.increaseReleaseNumById(coupon.getId());
                JobClientUtils.couponUseInfoInvalidTrigger(environment, jobClient, couponUseInfo.getId(), fetureDate);
                CardTicket ticket = new CardTicket();
                ticket.setStatus((short)CardTicketStatusEnum.EXCHANGED.getCode());
                ticket.setId(cardTicket.getId());
                ticket.setUserCouponCode(userCouponCode);
                ticketDao.update(ticket);
                return couponUseInfo;
            }else{
                throw new EquityException(MyErrorEnum.COUPON_USE_INFO_INSERT_ERROR);
            }
        }else{
            throw new EquityException(MyErrorEnum.COUPON_USE_INFO_INSERT_ERROR);
        }
    }

    @Override
    public List<CardTicketX> getCardTicket(String openId) {
        List<CardTicket> tickets = ticketDao.selectCardTicketByOpenId(openId);
        List<CardTicketX> ticketXES = new ArrayList<>();
        for(CardTicket ticket: tickets){
            CardTicketX cardTicketX = new CardTicketX();
            BeanUtils.copyProperties(ticket,cardTicketX);
            CardInfo infoX = infoDao.findById(ticket.getCardId());
            cardTicketX.setCardInfo(infoX);

            List<CouponBean> couponBeanList = new ArrayList<>();
            if(StringUtils.isNotEmpty(ticket.getUserCouponCode())){
                ArrayList<CouponUseInfoX> useInfoXList = new ArrayList<>();
                CouponUseInfo userCouponCode = useInfoDao.findByUserCouponCode(ticket.getUserCouponCode());
                if(userCouponCode != null){
                    CouponX coupon = couponDao.selectCouponXById(userCouponCode.getCouponId());
                    CouponBean couponBean = couponToBean(coupon);
                    couponBean.setCouponUseInfo(useInfoXList);
                    couponBeanList.add(couponBean);
                }
                CouponUseInfoX useInfoX = new CouponUseInfoX();
                BeanUtils.copyProperties(userCouponCode,useInfoX);
                useInfoXList.add(useInfoX);
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
            cardTicketX.setCoupons(couponBeanList);
            ticketXES.add(cardTicketX);
        }
        return ticketXES;
    }

    @Override
    public CardTicket findById(int id) {
        return ticketDao.findById(id);
    }

    @Override
    public CardTicket
    findByCardCode(String cardCode){
        return ticketDao.findbyCardCode(cardCode);
    }

    @Override
    public int invalid(int id) {
        if (1> id){
            log.error("invalid failed for id = {}",String.valueOf(id));
            return 0;
        }
        CardTicket record =ticketDao.findById(id);
        if(null == record){
            log.error("未找到id={}的记录",id);
            return 0;
        }
        if (CardTicketStatusEnum.canNotTimeout(record.getStatus())){
            log.error("该记录处于不可过期状态 {}", JSON.toJSONString(record));
            return 0;
        }
        CardTicket ticket = new CardTicket();
        ticket.setId(id);
        ticket.setStatus((short)CardTicketStatusEnum.TIMEOUT.getCode());
        return ticketDao.update(ticket);
    }

    @Override
    public Map<String, String> selectPlatformAll() {
        Map<String, String> platformMap = new HashMap<>();
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
        CardTicket ticket = ticketDao.selectCardTicketByCardOpenId(openId, card);
        CardTicketX ticketX = new CardTicketX();
        if(ticket != null){
            BeanUtils.copyProperties(ticket,ticketX);
            CardInfo infoX = infoDao.findById(ticket.getCardId());
            ticketX.setCardInfo(infoX);

            List<CouponBean> couponBeanList = new ArrayList<>();
            if(StringUtils.isNotEmpty(ticket.getUserCouponCode())){
                ArrayList<CouponUseInfoX> useInfoXList = new ArrayList<>();
                CouponUseInfo userCouponCode = useInfoDao.findByUserCouponCode(ticket.getUserCouponCode());
                if(userCouponCode != null){
                    CouponX coupon = couponDao.selectCouponXById(userCouponCode.getCouponId());
                    CouponBean couponBean = couponToBean(coupon);
                    couponBean.setCouponUseInfo(useInfoXList);
                    couponBeanList.add(couponBean);
                }
                CouponUseInfoX useInfoX = new CouponUseInfoX();
                BeanUtils.copyProperties(userCouponCode,useInfoX);
                useInfoXList.add(useInfoX);
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
            ticketX.setCoupons(couponBeanList);
        }
        return ticketX;
    }

    @Override
    public int deleteCardTicket(Integer id) {
        return ticketDao.deleteCardTicket(id);
    }

    @Override
    public List<Integer>
    getOrderIdByCouponId(List<Integer> couponIdList){

        List<CouponUseInfo> infoList = useInfoDao.selectByCouponIdList(couponIdList);
        List<Integer> orderIdList = new ArrayList<>();

        for(CouponUseInfo info: infoList){
            String[] idArray = info.getOrderId().split(",");
            for(String idString: idArray){
                Integer id = Integer.valueOf(idString);
                if (!orderIdList.contains(id)) {
                    orderIdList.add(id);
                }
            }
        }

        return orderIdList;
    }

    @Override
    public List<CouponUseInfo>
    selectByUserCouponCodeList(List<String> codeList){

        return useInfoDao.selectByUserCouponCodeList(codeList);
    }

    @Override
    public PageableData<CardTicket>
    findTickets(String activateStartTime, String activateEndTime,
                String consumeStartTime, String consumeEndTime,
                Integer status,
                CardTicketBean bean) {

        if(null != status){
            CardTicketStatusEnum.normalizeCode(status);
        }
        Date activeStart = DataUtils.String2Date(activateStartTime);
        Date activeEnd = DataUtils.String2Date(activateEndTime);
        Date consumeStart = DataUtils.String2Date(consumeStartTime);
        Date consumeEnd = DataUtils.String2Date(consumeEndTime);

        PageableData<CardTicket> pageableData = new PageableData<>();

        PageInfo<CardTicket> cardTicketPageInfo = ticketDao.getTicketPages(bean,status,activeStart,activeEnd,consumeStart,consumeEnd);
        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(cardTicketPageInfo);
        pageableData.setList(cardTicketPageInfo.getList());
        pageableData.setPageInfo(pageVo);

        return pageableData;
    }

    @Override
    public Integer
    putOpenIdByPhone(String openId, String phone,String appId){
        if(null == openId || null == phone || openId.isEmpty() || phone.isEmpty()){
            return 0;
        }

        String employeeCode = getEmployeeCodeByPhone(phone);
        if(null == employeeCode || employeeCode.isEmpty()){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }

        List<CardTicket> cardTickets = ticketDao.selectCardTicketByEmployeeCode(employeeCode);
        if(null == cardTickets || 0 == cardTickets.size()){
            log.error("没有找到员工编码={} 的提货卡",employeeCode);
            return 0;
        }
        log.info("找到员工编码={} 的提货卡 {}",employeeCode,JSON.toJSONString(cardTickets));

        List<Integer> cardInfoIdList = new ArrayList<>();
        cardTickets.forEach(cardTicket -> cardInfoIdList.add(cardTicket.getCardId()));

        ExportCardBean queryBean = new ExportCardBean();
        queryBean.setIds(cardInfoIdList);
        queryBean.setStatus((short)(int)CardInfoStatusEnum.RELEASED.getCode());
        List<CardInfo> cardInfoList = infoDao.findByIds(queryBean);
        List<String> cardInfoCodeList = new ArrayList<>();
        if(null == cardInfoList || 0 == cardInfoIdList.size()){
            log.error("没有找到员工编码={} 的兑换记录",employeeCode);
            return 0;
        }
        cardInfoList.forEach(cardInfo->{
            if(cardInfo.getAppId().equals(appId)){
                cardInfoCodeList.add(cardInfo.getCode());
            }
        });

        if(0 == cardInfoCodeList.size()){
            log.error("没有找到员工编码={} appId={} 的可用优惠券",employeeCode,appId);
            return 0;
        }

        return ticketDao.updateOpenIdByEmployeeCode(openId,employeeCode,cardInfoCodeList);
        //return ticketDao.selectByOpenId(openId,employeeCode);

    }

    @Override
    public int
    countTicketsCanRefund(String welfareOrderNo){
        List<CardTicket> tickets = ticketDao.getTicketsCanRefund(welfareOrderNo);
        return tickets.size();
    }

    @Override
    public Integer
    refundTickets(String welfareOrderNo, Integer count){

        List<CardTicket> tickets = ticketDao.getTicketsCanRefund(welfareOrderNo);
        if(null == tickets || 0 == tickets.size()){
            String errorMsg = "没有发现 welfareOrderNo="+welfareOrderNo+" 可以赎回的提货卡";
            log.error(errorMsg);
            return -1;

        }

        List<Integer> ticketIdList = new ArrayList<>();
        List<String> userCouponInfoCodeList = new ArrayList<>();
        if(null == count){
            tickets.forEach(ticket->{
                ticketIdList.add(ticket.getId());
                if(null != ticket.getUserCouponCode()){
                    userCouponInfoCodeList.add(ticket.getUserCouponCode());
                }
            });
        }else{
            int total = tickets.size();
            int i;
            for(i = 0; i < total && i < count; i++){
                CardTicket ticket = tickets.get(i);
                ticketIdList.add(ticket.getId());
                if(null != ticket.getUserCouponCode()){
                    userCouponInfoCodeList.add(ticket.getUserCouponCode());
                }
            }
        }
        log.info("赎回的提货卡 TicketIdList={}", JSON.toJSONString(ticketIdList));
        if (0 < userCouponInfoCodeList.size()) {
            List<CouponUseInfo> infos = useInfoDao.selectByUserCouponCodeList(userCouponInfoCodeList);

            List<Integer> couponIdList = new ArrayList<>();
            infos.forEach(info -> couponIdList.add(info.getCouponId()));

            log.info("赎回的提货卡 userCouponCodeList={}", JSON.toJSONString(userCouponInfoCodeList));
            log.info("赎回的提货卡 couponIdList={}", JSON.toJSONString(couponIdList));
            if(0 < couponIdList.size()) {
                couponDao.invalidCouponsByIdList(couponIdList);
            }
            useInfoDao.invalidUserCouponByCodeList(userCouponInfoCodeList);
        }
        int countTicket = ticketDao.invalidTicketsByIdList(ticketIdList);
        log.info("赎回的提货卡 数量={}", String.valueOf(countTicket));
        return countTicket;
    }

    private String
    getEmployeeCodeByPhone(String phone){

        OperaResult result = vendorsService.getEmployeeInfoByPhone(phone);
        log.info("服务调用用户返回: {}",JSON.toJSONString(result));
        if(200 != result.getCode()){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }
        Map<String,Object> data = result.getData();
        if (null == data){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }
        Object employeeObj = data.get("employee");
        if (null == employeeObj){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }
        log.info("服务调用用户返回: employee={}",JSON.toJSONString(employeeObj));
        JSONObject employee = JSON.parseObject(JSON.toJSONString(employeeObj));
        if (null == employee){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }

        Integer employeeStatus = employee.getInteger("status");
        String code = employee.getString("code");
        if (null == code || null == employeeStatus){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }

        if(1 == employeeStatus) {
            //INCUMBENT(1,"在职"),
            //OUTGOING(2,"离职"),
            return code;
        }else{
            log.error("phone={}的员工已经离职",phone);
            return null;
        }

    }

    private Map<String,Object>
    getEmployeeCodeMapByPhoneList(List<String> phoneList){
        String functionName = "服务调用批量获取员工号";
        log.info("{} 入参 {}",functionName,JSON.toJSONString(phoneList));
        OperaResult result = vendorsService.getEmployeeInfoByPhoneList(phoneList);
        log.info(JSON.toJSONString(result));
        if(200 != result.getCode()){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }

        Map<String,Object> map = result.getData();
        if (null == map){
            throw new EquityException(MyErrorEnum.EMPLOYEE_NOT_FIND_BY_PHONE);
        }
        log.info("{} 出参 {}",functionName,JSON.toJSONString(map));
        return map;

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
