package com.fengchao.equity.jobClient;

import com.fengchao.equity.bean.CouponBean;
import com.fengchao.equity.service.CouponService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

public class CouponInvalidRunnerJobImpl implements JobRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponInvalidRunnerJobImpl.class);

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行用户优惠券失效操作：" + jobContext);
            String id = jobContext.getJob().getParam("couponId") ;
            CouponService couponService = BeanContext.getApplicationContext().getBean(CouponService.class);
            int couponId = Integer.parseInt(id) ;
            CouponBean couponBean = couponService.findByCouponId(couponId);
            if (couponBean != null) {
                couponService.invalid(couponId) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("用户优惠券失效成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
