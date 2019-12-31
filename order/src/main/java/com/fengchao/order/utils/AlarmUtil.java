package com.fengchao.order.utils;

import com.fengchao.order.task.AlarmTask;
import com.fengchao.order.threadpool.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @Author tom
 * @Date 19-9-29 上午11:30
 */
@Slf4j
public class AlarmUtil {

    private static ExecutorService alarmExecutor = ThreadPoolManager
            .createThreadPoolExecutor("orderAlarmTaskPool", 20, 50, 300);



    /**
     * 报警
     * 异步发送报警邮件
     *
     * @param title
     * @param content
     */
    public static void alarmAsync(String title, String content) {
        try {
            log.info("报警alarmAsync执行开始 title:{}, content:{}", title, content);
            AlarmTask alarmTask = new AlarmTask(title, content);

            alarmExecutor.execute(alarmTask);
            log.info("报警alarmAsync执行结束 title:{}, content:{}", title, content);
        } catch (Exception e) {
            log.info("报警alarmAsync执行异常:{}", e.getMessage(), e);
        }

    }

}