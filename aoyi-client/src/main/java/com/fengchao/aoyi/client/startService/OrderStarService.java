package com.fengchao.aoyi.client.startService;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.starBean.StarOrderBean;

/**
 * @author songbw
 * @date 2020/1/2 15:01
 */
public interface OrderStarService {

    OperaResponse addOrder(StarOrderBean bean) ;
}
