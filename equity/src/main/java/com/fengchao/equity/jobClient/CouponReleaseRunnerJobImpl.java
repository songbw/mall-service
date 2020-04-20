package com.fengchao.equity.jobClient;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.model.CouponUseInfo;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.CouponUseStatusEnum;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class CouponReleaseRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponReleaseRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行优惠券释放操作：" + jobContext);
            String id = jobContext.getJob().getParam("couponUserId") ;
            CouponUseInfoService couponUseInfoService = BeanContext.getApplicationContext().getBean(CouponUseInfoService.class);
            int couponUserId = Integer.parseInt(id) ;
            CouponUseInfo couponUseInfo = couponUseInfoService.findBycouponUserId(couponUserId);
            if (couponUseInfo != null &&
                    couponUseInfo.getStatus() == CouponUseStatusEnum.OCCUPIED.getCode()) {
                couponUseInfoService.triggerRelease(couponUserId) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("优惠券释放成功");
            }else{
                LOGGER.warn("执行优惠券释放操作： 没有找到 id={} 的CouponUseInfo 或者处于非占用状态 {}",id,
                        (null == couponUseInfo)?"null": JSON.toJSON(couponUseInfo));
            }
        } catch (Exception e) {
            LOGGER.error("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "优惠券释放执行成功");
    }
}
