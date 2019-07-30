package com.fengchao.equity.jobClient;

import com.fengchao.equity.model.PromotionX;
import com.fengchao.equity.service.PromotionService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class PromotionEffectiveRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionEffectiveRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行活动生效操作：" + jobContext);
            String id = jobContext.getJob().getParam("promotionId") ;
            PromotionService promotionService = BeanContext.getApplicationContext().getBean(PromotionService.class);
            int promotionId = Integer.parseInt(id) ;
            PromotionX promotion = promotionService.findPromotionById(promotionId);
            //已发布状态才会触发开始状态
            if (promotion != null && promotion.getStatus() ==3 ) {
                promotionService.effective(promotionId) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("活动生效成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
