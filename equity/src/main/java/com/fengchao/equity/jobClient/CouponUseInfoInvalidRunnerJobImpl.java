package com.fengchao.equity.jobClient;

import com.fengchao.equity.bean.CouponBean;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.service.CouponUseInfoService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

public class CouponUseInfoInvalidRunnerJobImpl implements JobRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponUseInfoInvalidRunnerJobImpl.class);

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行用户礼品卡绑定的优惠券失效操作：" + jobContext);
            String id = jobContext.getJob().getParam("couponUseId") ;
            CouponUseInfoService couponUseInfoService = BeanContext.getApplicationContext().getBean(CouponUseInfoService.class);
            int couponUseId = Integer.parseInt(id) ;
            CouponUseInfo couponUseInfo = couponUseInfoService.findBycouponUseId(couponUseId);
            if (couponUseInfo != null) {
                couponUseInfoService.couponUseInvalid(couponUseId) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("用户礼品卡绑定的优惠券失效成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
