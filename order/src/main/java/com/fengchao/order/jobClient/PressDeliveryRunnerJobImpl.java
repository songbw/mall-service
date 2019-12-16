package com.fengchao.order.jobClient;

import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.rpc.BaseRpcService;
import com.fengchao.order.rpc.VendorsRpcService;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import com.fengchao.order.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发货提醒短信
 *
 * 1. 每天定时对有发货订单的供应商发送短信提醒
 * 2. 发送短信提醒完成后, 发送确认邮件给运营同学
 */
@Slf4j
public class PressDeliveryRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PressDeliveryRunnerJobImpl.class);

    /**
     * 发货提醒短信发送模版
     */
    private static final Integer SMS_TEMPLATE = 494125;

    /**
     * 邮件接收人
     */
    private static String MAIL_ADDRESS = "alicia.yang@weesharing.com,tom.jing@weesharing.com,jon.zhou@weesharing.com";

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("供应商发货短信提醒任务执行时间");
        //
        BaseRpcService baseRpcService = BeanContext.getBean(BaseRpcService.class);

        try {
            log.info("供应商发货短信提醒任务 开始..");

            // 1. 获取所有供应商
            VendorsRpcService vendorsRpcService = BeanContext.getBean(VendorsRpcService.class);
            List<SysCompanyX> sysCompanyXList = vendorsRpcService.queryAllCompanyList();
            // 转map key: merchantId, value: SysCompanyX
            Map<Long, SysCompanyX> merchantMap =
                    sysCompanyXList.stream().collect(Collectors.toMap(s -> s.getId(), s -> s));

            log.info("供应商发货短信提醒任务 获取的供应商信息:{}", JSONUtil.toJsonString(merchantMap));

            // 2.获取待发货的子订单
            OrderDetailDao orderDetailDao = BeanContext.getBean(OrderDetailDao.class);
            List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByStatus(1);
            log.info("供应商发货短信提醒任务 获取未发货的订单信息:{}", JSONUtil.toJsonString(orderDetailList));

            if (CollectionUtils.isEmpty(orderDetailList)) {
                log.warn("供应商发货短信提醒任务 未获取到待发货的订单信息");

                baseRpcService.sendMail(MAIL_ADDRESS.split(","),
                        "供应商发货短信提醒任务",
                        "未获取到待发货的订单信息 :: traceId=" + MDC.get("X-B3-TraceId"));

                return new Result(Action.EXECUTE_SUCCESS, "供应商发货短信提醒任务 执行完成!!!");
            }

            // 2.1 转map 以merchant为维度, key:merchantId, value: 该商户未发货订单数量
            Map<Long, Integer> smsMap = new HashMap<>();
            for (OrderDetail orderDetail : orderDetailList) {
                Long _merchantId = Long.valueOf(orderDetail.getMerchantId());

                if (smsMap.get(_merchantId) == null) {
                    smsMap.put(_merchantId, 1);
                } else {
                    smsMap.put(_merchantId, smsMap.get(_merchantId) + 1);
                }
            }
            log.info("供应商发货短信提醒任务 获取供应商未发货的对应关系:{}", JSONUtil.toJsonString(smsMap));

            // 3. 开始发短信
            // 记录发送结果 供应商 + 待发货数 + 发送结果
            StringBuilder mailContent = new StringBuilder();

            if (smsMap.size() > 0) {
                int index = 1;
                for (Long key : smsMap.keySet()) { // 遍历map进行发送
                    String count = String.valueOf(smsMap.get(key)); //
                    SysCompanyX sysCompanyX = merchantMap.get(key);

                    if (sysCompanyX != null) {
                        if (StringUtils.isNotBlank(sysCompanyX.getPhone())) {
                            // 执行发送
                            String sendResult =
                                    baseRpcService.sendWithTemplate(sysCompanyX.getPhone(), SMS_TEMPLATE, new String[]{count});

                            mailContent.append(index).append(".").append(sysCompanyX.getName()).append("; 待发货数量:").append(count).append("; 发送结果:").append(sendResult).append("\r\n");
                        } else {
                            mailContent.append(index).append(".").append(sysCompanyX.getName()).append("; 待发货数量:").append(count).append("; 未找到联系电话").append("\r\n");
                        }
                    } else {
                        mailContent.append(index).append(".").append("未匹配供应商:").append(key).append("; 待发货数量:").append(count).append("\r\n");
                    }

                    index++;
                }
            }

            // 4. 发送邮件通知相关同学
            baseRpcService.sendMail(MAIL_ADDRESS.split(","),
                    "供应商发货短信提醒任务",
                    mailContent.toString() + " :: traceId=" + MDC.get("X-B3-TraceId"));
        } catch (Exception e) {
            LOGGER.error("供应商发货短信提醒任务 异常:{}", e.getMessage(), e);

            baseRpcService.sendMail(MAIL_ADDRESS.split(","),
                    "供应商发货短信提醒任务",
                    "任务执行失败 :: traceId=" + MDC.get("X-B3-TraceId"));
        } finally {
            stopWatch.stop();

            log.info("供应商发货短信提醒任务 耗时(秒):{}", stopWatch.getTotalTimeSeconds());
        }

        log.info("供应商发货短信提醒任务 执行完成");
        return new Result(Action.EXECUTE_SUCCESS, "供应商发货短信提醒任务 执行完成!");
    }
}
