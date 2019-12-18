package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.model.ProdExtendX;

public interface ProdExtendService {
    int updateProdExtend(ProdExtendX bean);

    int deleteProdExtend(String skuid);
}
