package com.fengchao.equity.jobClient;

import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.service.VirtualTicketsService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class VirtualInvalidRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualInvalidRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行虚拟券结束操作：" + jobContext);
            String id = jobContext.getJob().getParam("virtualId") ;
            VirtualTicketsService service = BeanContext.getApplicationContext().getBean(VirtualTicketsService.class);
            int virtualId = Integer.parseInt(id) ;
            VirtualTickets tickets = service.findVirtualTicketById(virtualId);
            if (tickets != null && tickets.getStatus() != 3) {
                service.ticketsInvalid(virtualId);
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("虚拟券结束成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
