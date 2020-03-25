package com.fengchao.freight.exception;

import com.fengchao.freight.constant.MyErrorEnum;

/**
 * @author clark
 */
public class MyException extends RuntimeException {

    public MyException(MyErrorEnum myErrorEnum){

        super(myErrorEnum.buildCodeMsg());
    }
}
