package com.fengchao.product.aoyi.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by tom on 22/8/17.
 */
@Slf4j
@EnableScheduling
// @Component
public class ThreadMonitor {

    private String hostName;

    public ThreadMonitor() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName().toString(); // 获取本机计算机名称

            this.hostName = hostName;

            log.info("本机计算机名:{}", hostName);
        } catch (UnknownHostException e) {
            log.error("获取本机计算机名称出错啦:{}", e.getMessage(), e);
        }
    }

    // @Scheduled(cron = "0 */1 * * * *")
    public void monitor() {
        Map<String, ThreadPoolExecutor> poolMap = ThreadPoolManager.getThreadPoolExecutorMap();
        poolMap.forEach((key, value) -> {
            int queueUsed = value.getQueue().size();
            int queueRemain = value.getQueue().remainingCapacity();
            int coreSize = value.getCorePoolSize();
            int maxSize = value.getMaximumPoolSize();
            int activeSize = value.getActiveCount();
            int largestSize = value.getLargestPoolSize();

            // 队列大小 10 / 2000, 线程池core: 10 / 20, Active线程: 10 / 50, max线程:
            String logContent = String.format("线程池: %s, 队列大小 %d / %d, 线程池core: %d, Active线程: %d / %d, largest线程: %d",
                    key, queueUsed, queueRemain, coreSize, activeSize, maxSize, largestSize);

            log.info(logContent);

//            if (queueUsed >= queueRemain) {
//                Cat.logEvent("BUSINESSPOOL", hostName + "_" + key, "-1", logContent); // CatConstants.CAT_EVENT_FAILED
//            } else {
//                Cat.logEvent("BUSINESSPOOL", hostName + "_" + key, Event.SUCCESS, logContent);
//            }

        });
    }

    public static void main(String args[]) {
    }

}
