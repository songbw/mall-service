package com.fengchao.equity.service;

import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.model.VirtualProdX;

public interface VirtualProdService {
    int createVirtualProd(VirtualProd bean);

    PageableData<VirtualProd> findVirtualProd(Integer pageNo, Integer pageSize);

    VirtualProdX findByVirtualProdId(Integer id);

    int updateVirtualProd(VirtualProd bean);

    VirtualProdX findByVirtualProdMpu(String mpu);

    int deleteVirtualProd(Integer id);
}
