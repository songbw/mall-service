package com.fengchao.equity.controller;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author tom
 * @Date 19-8-2 下午3:49
 */
@RestController
@RequestMapping(value = "/equity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class HelloController {

    /**
     * http://localhost:8007/equity/hello?flag=1 // 失败请求
     *
     * @param flag
     * @return
     */
    @GetMapping("hello")
    public String hello(@RequestParam(value = "flag", required = false) String flag) {
        log.info("equity service work!");

        Transaction t = Cat.newTransaction("Equity-Hello", "hello");

        try {
            if ("1".equals(flag)) {
                Cat.logEvent("hello.test", "test", "-1", "test=hellooo");

                throw new Exception("测试");
            } else {
                Cat.logEvent("hello.test", "test", Event.SUCCESS, "test=hellooo");
            }
//            Cat.logMetricForCount("metric.key");
//            Cat.logMetricForDuration("metric.key", 5);


            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logError(e);
        } finally {
            t.complete();
        }

        return "equity service work!";
    }
}
