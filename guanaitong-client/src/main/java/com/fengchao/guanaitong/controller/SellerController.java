package com.fengchao.guanaitong.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.guanaitong.service.impl.GuanAiTongServiceImpl;
import com.fengchao.guanaitong.util.GuanAiTong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SellerController {

    private GuanAiTongServiceImpl guanAiTongService;

    @Autowired
    public SellerController(GuanAiTongServiceImpl guanAiTongService) {
        this.guanAiTongService = guanAiTongService;

    }

    private void build400Response(HttpServletResponse response, String msg
                                ) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        JSONObject json = new JSONObject();

        json.put("code", 400);
        json.put("msg", msg);
        json.put("data",null);
        try {
            out = response.getWriter();
            out.append(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void buildResponse(HttpServletResponse response,
                               JSONObject json
                              ) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        Integer code = json.getInteger("code");
        if (null != code && 0 == code) {
            json.put("code", 200);
        }
        if (null == code) {
            json.put("code", 400);
        }

        try {
            out = response.getWriter();
            out.append(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_OPEN_ID_PATH)
    public void getOpenId(HttpServletResponse response,
                          @RequestBody Map<String, Object> m
                          ) {
        log.info("getOpenId parameter : {}", JSON.toJSONString(m));
        Map<String, Object> map = new TreeMap<>();
        if (null == m.get(GuanAiTong.AUTH_CODE_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.AUTH_CODE_KEY);
            return;
        }
        String authCode = m.get(GuanAiTong.AUTH_CODE_KEY).toString();
        if (null == authCode || authCode.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.AUTH_CODE_KEY);
            return;
        }
        map.put(GuanAiTong.AUTH_CODE_KEY,authCode);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_OPEN_ID_PATH, map);
            if (null != json) {
                log.info("guanAiTongPost {} got response : {}",GuanAiTong.GET_OPEN_ID_PATH, json.toJSONString());
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.GET_OPEN_ID_PATH,ex.getMessage());
        }
        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanAiTong");
    }
/*
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_ENTERPRISE_OPEN_ID_PATH)
    public void getEnterpriseOpenId(HttpServletResponse response,
                          @RequestBody Map<String, String> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_ENTERPRISE_OPEN_ID_PATH, map);
            if (null == json) {
                log.info("GuanAiTong response data is NULL");
            }
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
            build400Response(response, "failed to access guanaitong");
        }

        buildResponse(response, json);
    }
*/

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_DETAIL_PATH)
    public void getDetail(HttpServletResponse response,
                          @RequestBody Map<String, Object> m
                        ) {

        log.info("getDetail parameter : {}", JSON.toJSONString(m));
        Map<String, Object> map = new TreeMap<>();
        if (null == m.get(GuanAiTong.OPEN_ID_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        String openId = m.get(GuanAiTong.OPEN_ID_KEY).toString();
        if (null == openId || openId.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        map.put(GuanAiTong.OPEN_ID_KEY,openId);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_DETAIL_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.GET_DETAIL_PATH,ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_ENTERPRISE_INFO_PATH)
    public void getEnterpriseDetail(HttpServletResponse response,
                                    @RequestBody Map<String, Object> m
                                    ) {
        log.info("getEnterpriseDetail parameter : {}", JSON.toJSONString(m));
        if (null == m.get(GuanAiTong.OPEN_ID_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();

        String openId = m.get(GuanAiTong.OPEN_ID_KEY).toString();
        if (null == openId || openId.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        map.put(GuanAiTong.OPEN_ID_KEY,openId);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_ENTERPRISE_INFO_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.GET_ENTERPRISE_INFO_PATH,ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.SEND_MESSAGE_PATH)
    public void sendMessage(HttpServletResponse response,
                            @RequestBody Map<String, Object> m
                            ) {

        log.info("sendMessage parameter : {}", JSON.toJSONString(m));
        if (null == m.get(GuanAiTong.OPEN_ID_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();
        String openId = m.get(GuanAiTong.OPEN_ID_KEY).toString();
        if (null == openId || openId.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OPEN_ID_KEY);
            return;
        }
        map.put(GuanAiTong.OPEN_ID_KEY,openId);

        if (null == m.get(GuanAiTong.CODE_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.CODE_KEY);
            return;
        }
        String theCode = m.get(GuanAiTong.CODE_KEY).toString();
        if (null == theCode || theCode.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.CODE_KEY);
            return;
        }
        map.put(GuanAiTong.CODE_KEY, theCode);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.SEND_MESSAGE_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.SEND_MESSAGE_PATH,ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanAiTong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.POST_SYNC_REFUND_PATH)
    public void postRefund(HttpServletResponse response,
                            @RequestBody Map<String, Object> m
                            ) {

        log.info("postRefund parameter ; {}", JSON.toJSONString(m));
        if (null == m.get(GuanAiTong.OUTER_TRADE_NO_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_TRADE_NO_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();

        try {
            Object oo = m.get(GuanAiTong.OUTER_TRADE_NO_KEY);
            if (null == oo) {
                build400Response(response, "missing parameter: " + GuanAiTong.OUTER_TRADE_NO_KEY);
                return;
            }
            String outTradeNo = m.get(GuanAiTong.OUTER_TRADE_NO_KEY).toString();
            if (null == outTradeNo || outTradeNo.isEmpty()) {
                build400Response(response, "wrong parameter: " + GuanAiTong.OUTER_TRADE_NO_KEY);
                return;
            }
            map.put(GuanAiTong.OUTER_TRADE_NO_KEY, outTradeNo);

            if (null == m.get(GuanAiTong.OUTER_REFUND_NO_KEY)) {
                build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
                return;
            }
            String outRefundNo = m.get(GuanAiTong.OUTER_REFUND_NO_KEY).toString();
            if (null == outRefundNo || outRefundNo.isEmpty()) {
                build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
                return;
            }
            map.put(GuanAiTong.OUTER_REFUND_NO_KEY, outRefundNo);

            if (null == m.get(GuanAiTong.REASON_KEY)) {
                build400Response(response, "missing parameter: " + GuanAiTong.REASON_KEY);
                return;
            }
            String reason = m.get(GuanAiTong.REASON_KEY).toString();
            if (null == reason || reason.isEmpty()) {
                build400Response(response, "missing parameter: " + GuanAiTong.REASON_KEY);
                return;
            }
            map.put(GuanAiTong.REASON_KEY, reason);

            if (null == m.get(GuanAiTong.REFUND_AMOUNT_KEY)) {
                build400Response(response, "missing parameter: " + GuanAiTong.REFUND_AMOUNT_KEY);
                return;
            }
            Double refundAmount = (Double) m.get(GuanAiTong.REFUND_AMOUNT_KEY);
            if (null == refundAmount) {
                build400Response(response, "missing parameter: " + GuanAiTong.REFUND_AMOUNT_KEY);
                return;
            }
            map.put(GuanAiTong.REFUND_AMOUNT_KEY, refundAmount);

            Integer costAmount = (Integer) m.get(GuanAiTong.COST_AMOUNT_KEY);
            if (null != costAmount) {
                map.put(GuanAiTong.COST_AMOUNT_KEY, costAmount);
            }

            Integer deliveryFee = (Integer) m.get(GuanAiTong.DELIVERY_FEE_KEY);
            if (null != deliveryFee) {
                map.put(GuanAiTong.DELIVERY_FEE_KEY, deliveryFee);
            }

            if (null != m.get(GuanAiTong.NOTIFY_URL_KEY)) {
                String notifyUrl = m.get(GuanAiTong.NOTIFY_URL_KEY).toString();
                if (null != notifyUrl && !notifyUrl.isEmpty()) {
                    map.put(GuanAiTong.NOTIFY_URL_KEY, notifyUrl);
                }
            }
        } catch (Exception e) {
            log.info("postRefund, checking parameters error: " + e.getMessage());
            build400Response(response, "wrong parameter in post body");
            return;
        }
        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_V2_REFUND_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.POST_V2_REFUND_PATH,ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_PAY_RECORD_PATH)
    public void getPayRecord(HttpServletResponse response,
                           @RequestBody Map<String, Object> m
                            ) {

        log.info("getPayRecord parameter : {}", JSON.toJSONString(m));
        if (null == m.get(GuanAiTong.OUTER_TRADE_NO_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_TRADE_NO_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();
        String outTradeNo = m.get(GuanAiTong.OUTER_TRADE_NO_KEY).toString();
        if (null == outTradeNo || outTradeNo.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_TRADE_NO_KEY);
            return;
        }
        map.put(GuanAiTong.OUTER_TRADE_NO_KEY,outTradeNo);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_PAY_RECORD_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.GET_PAY_RECORD_PATH,ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_REFUND_RECORD_PATH)
    public void getRdfundRecord(HttpServletResponse response,
                             @RequestBody Map<String, String> m
                            ) {

        log.info("getRdfundRecord parameter : {}", JSON.toJSONString(m));
        if (null == m.get(GuanAiTong.OUTER_REFUND_NO_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();
        String outRefundNo = m.get(GuanAiTong.OUTER_REFUND_NO_KEY);
        if (null == outRefundNo || outRefundNo.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
            return;
        }
        map.put(GuanAiTong.OUTER_REFUND_NO_KEY,outRefundNo);

        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_REFUND_RECORD_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (Exception ex) {
            log.info("guanAiTongPost {} got exception : {}",GuanAiTong.GET_REFUND_RECORD_PATH, ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
    }
/*
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.POST_ORDER_DELIVERY_PATH)
    public void sendOrderDelivery(HttpServletResponse response,
                             @RequestBody Map<String, Object> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_ORDER_DELIVERY_PATH, map);
            if (null == json) {
                log.info("GuanAiTong response data is NULL");
            }
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
            System.out.println("==== post got exception");
        }

        buildResponse(response, json);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_PERSON_ADDRESS_PATH)
    public void getPersonAddress(HttpServletResponse response,
                             @RequestBody Map<String, String> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_PERSON_ADDRESS_PATH, map);
            if (null == json) {
                log.info("GuanAiTong response data is NULL");
            }
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
            System.out.println("==== post got exception");
        }

        buildResponse(response, json);
    }
*/
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_SIGN_PARAM_PATH)
    public void getSignParam(HttpServletResponse response,
                                 @RequestBody Map<String, Object> map
                            ) {

        log.info("getSignParam parameter : {}", JSON.toJSONString(map));
        JSONObject json = new JSONObject();

        try {
            String signedParam = guanAiTongService.buildUrlXFormBody(map);
            if (null == signedParam) {
                json.put("code",400);
                json.put("data", null);
                json.put("msg", "Failed to sign");
            } else {
                json.put("code", 200);
                json.put("data", signedParam);
                json.put("msg", "");
            }

        } catch (Exception ex) {
            log.info("getSignParam got exception : {}",ex.getMessage());
            build400Response(response,"failed to sign parameter");
            return;
        }

        log.info("getSignParam got sign : {}", json.toJSONString());
        buildResponse(response, json);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.POST_TRADE_INFO_PATH)
    public void postTradeInfo(HttpServletResponse response,
                              @RequestBody Map<String, Object> map
    ) {

        log.info("===postTradeInfo 入参：{}",JSON.toJSONString(map));

        JSONObject result = new JSONObject();
        Object tradeNo = map.get(GuanAiTong.OUTER_TRADE_NO_KEY);
        Object tradeInfo = map.get(GuanAiTong.TRADE_INFO_KEY);
        if (null == tradeNo || null == tradeInfo || tradeInfo.toString().isEmpty() || tradeNo.toString().isEmpty()){
            String msg = "交易号，交易详情不可为空";
            log.error(msg);
            result.put("msg",msg);
            buildResponse(response, result);
            return;
        }

        String tradeInfoStr = JSON.toJSONString(tradeInfo);

        Map<String,Object> params = new TreeMap<>();
        params.put(GuanAiTong.OUTER_TRADE_NO_KEY,tradeNo);
        params.put(GuanAiTong.TRADE_INFO_KEY,tradeInfoStr);
        Object refundNo = map.get(GuanAiTong.OUTER_REFUND_NO_KEY);
        if (null != refundNo && null != refundNo.toString()){
            params.put(GuanAiTong.OUTER_REFUND_NO_KEY,refundNo.toString());
        }
        JSONObject json = new JSONObject();
        try {
            //json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_TRADE_INFO_PATH, params);
            json = guanAiTongService.guanAiTongXFormUrlEncodedPost(GuanAiTong.POST_TRADE_INFO_PATH, map);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            log.error(msg,ex);
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        /*
        JSONObject json;
        try {
            json = guanAiTongService.guanAiTongXFormUrlEncodedPost(GuanAiTong.POST_TRADE_INFO_PATH, map);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            log.error(msg,ex);
            json = new JSONObject();
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        if (null == json) {
            String msg = "GuanAiTong response data is NULL";
            log.error(msg);
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        */
        buildResponse(response, json);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping("tradeInfo")
    public void postWorkOrderTradeInfo(HttpServletResponse response,
                              @RequestBody Map<String, Object> map
    ) {

        log.info("===postTradeInfo 入参：{}",JSON.toJSONString(map));

        JSONObject result = new JSONObject();
        Object tradeNo = map.get(GuanAiTong.OUTER_TRADE_NO_KEY);
        Object tradeInfo = map.get(GuanAiTong.TRADE_INFO_KEY);
        if (null == tradeNo || null == tradeInfo || tradeInfo.toString().isEmpty() || tradeNo.toString().isEmpty()){
            String msg = "交易号，交易详情不可为空";
            log.error(msg);
            result.put("msg",msg);
            buildResponse(response, result);
            return;
        }

        String tradeInfoStr = JSON.toJSONString(tradeInfo);

        Map<String,Object> params = new TreeMap<>();
        params.put(GuanAiTong.OUTER_TRADE_NO_KEY,tradeNo);
        params.put(GuanAiTong.TRADE_INFO_KEY,tradeInfoStr);
        Object refundNo = map.get(GuanAiTong.OUTER_REFUND_NO_KEY);
        if (null != refundNo && null != refundNo.toString()){
            params.put(GuanAiTong.OUTER_REFUND_NO_KEY,refundNo.toString());
        }
        JSONObject json = new JSONObject();
        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_TRADE_INFO_PATH, params);
            //json = guanAiTongService.guanAiTongXFormUrlEncodedPost(GuanAiTong.POST_TRADE_INFO_PATH, map);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            log.error(msg,ex);
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        /*
        JSONObject json;
        try {
            json = guanAiTongService.guanAiTongXFormUrlEncodedPost(GuanAiTong.POST_TRADE_INFO_PATH, map);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            log.error(msg,ex);
            json = new JSONObject();
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        if (null == json) {
            String msg = "GuanAiTong response data is NULL";
            log.error(msg);
            json.put("msg",msg);
            buildResponse(response, json);
            return;
        }
        */
        buildResponse(response, json);
    }
}
