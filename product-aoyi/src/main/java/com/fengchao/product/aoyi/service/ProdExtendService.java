package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.model.ProdExtend;

public interface ProdExtendService {
    int updateProdExtend(ProdExtend bean);

    int deleteProdExtend(String skuid);
}
