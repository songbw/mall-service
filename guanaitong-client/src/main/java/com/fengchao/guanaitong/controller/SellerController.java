package com.fengchao.guanaitong.controller;

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
            out.append(json.toString());
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
            out.append(json.toString());
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
                buildResponse(response, json);
                return;
            }
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }
        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
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
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_ENTERPRISE_INFO_PATH)
    public void getEnterpriseDetail(HttpServletResponse response,
                                    @RequestBody Map<String, Object> m
                                    ) {

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
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.SEND_MESSAGE_PATH)
    public void sendMessage(HttpServletResponse response,
                            @RequestBody Map<String, Object> m
                            ) {

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
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");

    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.POST_SYNC_REFUND_PATH)
    public void postRefund(HttpServletResponse response,
                            @RequestBody Map<String, Object> m
                            ) {

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

        if (null == m.get(GuanAiTong.OUTER_REFUND_NO_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
            return;
        }
        String outRefundNo = m.get(GuanAiTong.OUTER_REFUND_NO_KEY).toString();
        if (null == outRefundNo || outRefundNo.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
            return;
        }
        map.put(GuanAiTong.OUTER_REFUND_NO_KEY,outRefundNo);

        if (null == m.get(GuanAiTong.REASON_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.REASON_KEY);
            return;
        }
        String reason = m.get(GuanAiTong.REASON_KEY).toString();
        if (null == reason || reason.isEmpty()) {
            build400Response(response, "missing parameter: " + GuanAiTong.REASON_KEY);
            return;
        }
        map.put(GuanAiTong.REASON_KEY,reason);

        if (null == m.get(GuanAiTong.REFUND_AMOUNT_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.REFUND_AMOUNT_KEY);
            return;
        }
        Integer refundAmount = (Integer) m.get(GuanAiTong.REFUND_AMOUNT_KEY);
        if (null == refundAmount) {
            build400Response(response, "missing parameter: " + GuanAiTong.REFUND_AMOUNT_KEY);
            return;
        }
        map.put(GuanAiTong.REFUND_AMOUNT_KEY,refundAmount);

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
        try {
            JSONObject json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_SYNC_REFUND_PATH, map);
            if (null != json) {
                buildResponse(response, json);
                return;
            }
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_PAY_RECORD_PATH)
    public void getPayRecord(HttpServletResponse response,
                           @RequestBody Map<String, Object> m
    ) {

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
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
        }

        log.info("GuanAiTong response data is NULL");
        build400Response(response, "failed to access guanaitong");
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_REFUND_RECORD_PATH)
    public void getRdfundRecord(HttpServletResponse response,
                             @RequestBody Map<String, String> m
    ) {

        if (null == m.get(GuanAiTong.OUTER_REFUND_NO_KEY)) {
            build400Response(response, "missing parameter: " + GuanAiTong.OUTER_REFUND_NO_KEY);
            return;
        }
        Map<String, Object> map = new TreeMap<>();
        String outRefundNo = m.get(GuanAiTong.OUTER_REFUND_NO_KEY).toString();
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
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
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

        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
            System.out.println("==== post got exception");
        }

        buildResponse(response, json);
    }
}
