package com.fengchao.base.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.fengchao.base.config.SMSConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Slf4j
@Component
public class SMSUtil {

    /**
     * 自定义短信内容发送
     * @param msg 短信内容
     * @param number 用户手机号
     * @return OK 成功  null 失败
     */
    public static String sendMess(String msg , String number){
        try {
            SmsSingleSender ssender = new SmsSingleSender(SMSConfig.TENT_AppkeyTXAPP_ID, SMSConfig.TENT_AppSecretTXAPP_KEY);
            SmsSingleSenderResult result = ssender.send(0, "86", number,
                    msg, "", "");
            return result.errMsg;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定模板ID发送短信
     *
     * @param number 用户手机号
     * @return OK 成功  其他 失败
     *
     * 失败:
     * {
     *   "errMsg": "package format error, template params error",
     *   "ext": "",
     *   "fee": 0,
     *   "response": {
     *     "body": "{\"result\":1014,\"errmsg\":\"package format error, template params error\",\"ext\":\"\"}",
     *     "headers": {
     *       "Keep-Alive": "timeout=86400",
     *       "Server": "nginx",
     *       "Access-Control-Allow-Origin": "*",
     *       "Access-Control-Allow-Methods": "POST",
     *       "Connection": "keep-alive",
     *       "Content-Length": "79",
     *       "Date": "Fri, 13 Dec 2019 03:55:42 GMT",
     *       "Access-Control-Allow-Headers": "X-Requested-With,Content-Type",
     *       "Content-Type": "application/json; charset=utf-8"
     *     },
     *     "reason": "OK",
     *     "request": {
     *       "body": "{\"sig\":\"03b79eea5a4a56a2c99c792b0f7c0aa17e4318116c3a3b8635ff727cfe7d0837\",\"extend\":\"\",\"ext\":\"\",\"tpl_id\":416933,\"tel\":{\"mobile\":\"13488689297\",\"nationcode\":\"86\"},\"time\":1576209342,\"params\":[\"7\"]}",
     *       "bodyCharset": "UTF-8",
     *       "connectTimeout": 60000,
     *       "headers": {
     *         "Conetent-Type": "application/json"
     *       },
     *       "method": "POST",
     *       "parameters": {
     *         "random": "52771",
     *         "sdkappid": "1400255961"
     *       },
     *       "requestTimeout": 60000,
     *       "url": "https://yun.tim.qq.com/v5/tlssmssvr/sendsms"
     *     },
     *     "statusCode": 200
     *   },
     *   "result": 1014,
     *   "sid": ""
     * }
     *
     * 成功:
     * {
     *   "errMsg": "OK",
     *   "ext": "",
     *   "fee": 1,
     *   "response": {
     *     "body": "{\"result\":0,\"errmsg\":\"OK\",\"ext\":\"\",\"sid\":\"2063:196765650515762094973268929\",\"fee\":1}",
     *     "headers": {
     *       "Keep-Alive": "timeout=86400",
     *       "Server": "nginx",
     *       "Access-Control-Allow-Origin": "*",
     *       "Access-Control-Allow-Methods": "POST",
     *       "Connection": "keep-alive",
     *       "Content-Length": "84",
     *       "Date": "Fri, 13 Dec 2019 03:58:17 GMT",
     *       "Access-Control-Allow-Headers": "X-Requested-With,Content-Type",
     *       "Content-Type": "application/json; charset=utf-8"
     *     },
     *     "reason": "OK",
     *     "request": {
     *       "body": "{\"sig\":\"bb4de71181ebad57be2f584b20c427b63cab1b07957dcad5fdd3f216a95d298c\",\"extend\":\"\",\"ext\":\"\",\"tpl_id\":416933,\"tel\":{\"mobile\":\"13488689297\",\"nationcode\":\"86\"},\"time\":1576209497,\"params\":[\"098616\",\"5\"]}",
     *       "bodyCharset": "UTF-8",
     *       "connectTimeout": 60000,
     *       "headers": {
     *         "Conetent-Type": "application/json"
     *       },
     *       "method": "POST",
     *       "parameters": {
     *         "random": "21656",
     *         "sdkappid": "1400255961"
     *       },
     *       "requestTimeout": 60000,
     *       "url": "https://yun.tim.qq.com/v5/tlssmssvr/sendsms"
     *     },
     *     "statusCode": 200
     *   },
     *   "result": 0,
     *   "sid": "2063:196765650515762094973268929"
     * }
     */
    public static String sendMesModel(String[] params, String number, Integer template) throws Exception{
        try {
            log.info("指定模板ID单发短信 手机号:{}, 模版:{}", number, template);

            SmsSingleSender ssender = new SmsSingleSender(SMSConfig.TENT_AppkeyTXAPP_ID, SMSConfig.TENT_AppSecretTXAPP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", number,
                    template, params, null, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信

            log.info("指定模板ID单发短信 返回:{}", JSONUtil.toJsonString(result));

            return result.errMsg;
        } catch (HTTPException e) {
            // HTTP响应码错误
            log.error("指定模板ID单发短信HTTPException异常:{}", e.getMessage(), e);
            throw e;
        } catch (JSONException e) {
            // json解析错误
            log.error("指定模板ID单发短信JSONException异常:{}", e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            // 网络IO错误
            log.error("指定模板ID单发短信IOException异常:{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 群发自定义短信
     * @param msg 短信内容
     * @param numbers 用户手机号数组
     * @return OK 成功 null 失败
     */
    public static String sendMesModel(String msg , String[] numbers){
        try {
            SmsMultiSender msender = new SmsMultiSender(SMSConfig.TENT_AppkeyTXAPP_ID, SMSConfig.TENT_AppSecretTXAPP_KEY);
            SmsMultiSenderResult result =  msender.send(0, "86", numbers,
                    msg, "", "");
            System.out.print(result);
            return result.errMsg;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定模板ID群发
     * @param numbers 用户手机号数组
     * @return OK 成功 null 失败
     */
    public static String sendMesModel(String[] numbers){
        try {
            String[] params = {"hello" , "1" };
            SmsMultiSender msender = new SmsMultiSender(SMSConfig.TENT_AppkeyTXAPP_ID, SMSConfig.TENT_AppSecretTXAPP_KEY);
            SmsMultiSenderResult result =  msender.sendWithParam("86", numbers,
                    SMSConfig.TENT_TemplateID1, params, "我的钱包", "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
            return result.errMsg;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return null;
    }

    public static String getRandom() {
        Random random = new Random();
        String fourRandom = random.nextInt(1000000) + "";
        int randLength = fourRandom.length();
        if (randLength < 6) {
            for (int i = 1; i <= 6 - randLength; i++)
                fourRandom = "0" + fourRandom;
        }
        return fourRandom;
    }


}
