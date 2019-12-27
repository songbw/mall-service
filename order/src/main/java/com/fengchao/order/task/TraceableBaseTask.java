package com.fengchao.order.task;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public abstract class TraceableBaseTask implements Runnable {

    public String traceId;

    public TraceableBaseTask() {
        this.traceId = MDC.get("X-B3-TraceId");
    }


    @Override
    public void run() {
        MDC.put("X-B3-TraceId", traceId);

        execute();
    }

    abstract void execute();
}
