package com.fengchao.order.bean.bo;

import com.fengchao.order.rpc.extmodel.RefundDetailBean;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午2:22
 */
@Setter
@Getter
public class OrderDetailBo {

    private Integer id;

    private Integer merchantId;

    private Integer orderId;

    private String subOrderId;

    private String mpu;

    private String skuId;

    private Integer num;

    private Integer promotionId;

    private Float promotionDiscount;

    private BigDecimal salePrice;

    private BigDecimal unitPrice;

    private String image;

    private String model;

    private String name;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String logisticsId;

    private String logisticsContent;

    private String comcode;

    private Integer skuCouponDiscount;

    /**
     * 退款金额 单位元
     */
    private String refundAmount;

    private String remark;

    private BigDecimal sprice;

    /**
     * 该子订单的退款详情
     */
    private List<RefundDetailBean> refundDetailBeanList;

    /**
     * 0：待付款；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：已取消，申请售后
     *
     * @return
     */
    public String getOrderDetailStatus() {
        String _status = "未知";

        if (this.status != null) {
            switch (this.status) {
                case 0:
                    return "待付款";
                case 1:
                    return "待发货";
                case 2:
                    return "已发货";
                case 3:
                    return "已完成";
                case 4:
                    return "已取消";
                case 5:
                    return "已申请售后";
                default:
                    return _status;
            }
        }

        return _status;
    }
}
