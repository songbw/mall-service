package com.fengchao.product.aoyi.jobClient;

import com.fengchao.product.aoyi.service.ThirdProdService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class ImageDownUpRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownUpRunnerJobImpl.class);

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            LOGGER.info("我要执行图片下载上传操作：{}", JSONUtil.toJsonString(jobContext));
            ThirdProdService thirdProdService = BeanContext.getApplicationContext().getBean(ThirdProdService.class) ;
            thirdProdService.uploadProdImage();
            bizLogger.info("图片下载上传成功");
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
