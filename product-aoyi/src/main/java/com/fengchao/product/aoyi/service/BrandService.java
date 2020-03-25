package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.AoyiBaseBrandX;

public interface BrandService {
    OperaResponse insertOrUpdate(AoyiBaseBrandX bean);
}
