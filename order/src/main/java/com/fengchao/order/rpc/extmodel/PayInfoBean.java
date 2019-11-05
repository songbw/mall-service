package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PayInfoBean {
    private List<OrderPayMethodInfoBean> list;
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    private Integer totalPage;
}
