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

@Slf4j
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SellerController {

    private GuanAiTongServiceImpl guanAiTongService;

    @Autowired
    public SellerController(GuanAiTongServiceImpl guanAiTongService) {
        this.guanAiTongService = guanAiTongService;

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
                          @RequestBody Map<String, String> map
                          ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_OPEN_ID_PATH, map);
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
            System.out.println("==== post got exception");
        }

        buildResponse(response, json);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(GuanAiTong.GET_DETAIL_PATH)
    public void getDetail(HttpServletResponse response,
                          @RequestBody Map<String, String> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_DETAIL_PATH, map);
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
    @PostMapping(GuanAiTong.GET_ENTERPRISE_INFO_PATH)
    public void getEnterpriseDetail(HttpServletResponse response,
                                    @RequestBody Map<String, String> map
                                    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_ENTERPRISE_INFO_PATH, map);
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
    @PostMapping(GuanAiTong.SEND_MESSAGE_PATH)
    public void sendMessage(HttpServletResponse response,
                            @RequestBody Map<String, String> map
                            ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.SEND_MESSAGE_PATH, map);
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
    @PostMapping(GuanAiTong.POST_SYNC_REFUND_PATH)
    public void postRefund(HttpServletResponse response,
                            @RequestBody Map<String, Object> map
                            ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.POST_SYNC_REFUND_PATH, map);
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
    @PostMapping(GuanAiTong.GET_PAY_RECORD_PATH)
    public void getPayRecord(HttpServletResponse response,
                           @RequestBody Map<String, String> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_PAY_RECORD_PATH, map);
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
    @PostMapping(GuanAiTong.GET_REFUND_RECORD_PATH)
    public void getRdfundRecord(HttpServletResponse response,
                             @RequestBody Map<String, String> map
    ) {

        JSONObject json = new JSONObject();

        try {
            json = guanAiTongService.guanAiTongPost(GuanAiTong.GET_REFUND_RECORD_PATH, map);
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
}
