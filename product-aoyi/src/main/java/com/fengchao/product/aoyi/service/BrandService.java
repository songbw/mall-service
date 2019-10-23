package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;

public interface BrandService {
    OperaResponse insertOrUpdate(AoyiBaseBrand bean);
}
