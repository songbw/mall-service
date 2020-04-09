package com.fengchao.equity.jobClient;

import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.service.CardTicketService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

public class CardInvalidRunnerJobImpl implements JobRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardInvalidRunnerJobImpl.class);

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行用户礼券卡失效操作：" + jobContext);
            String cardCode = jobContext.getJob().getParam("cardCode") ;
            CardTicketService cardTicketService = BeanContext.getApplicationContext().getBean(CardTicketService.class);

            CardTicket ticket = cardTicketService.findByCardCode(cardCode);
            if (ticket != null) {
                cardTicketService.invalid(ticket.getId()) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("用户礼券卡失效成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
