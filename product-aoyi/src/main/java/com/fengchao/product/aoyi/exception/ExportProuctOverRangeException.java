package com.fengchao.product.aoyi.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author tom
 * @Date 19-8-30 下午5:40
 */
@Setter
@Getter
public class ExportProuctOverRangeException extends RuntimeException {

    private static final Integer code = 416;

    private static final  String message = "导出数据量过大";

    public ExportProuctOverRangeException() {
        super(message);
    }
}
