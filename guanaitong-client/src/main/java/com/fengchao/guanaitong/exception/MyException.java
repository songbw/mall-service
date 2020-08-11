package com.fengchao.guanaitong.exception;

import com.fengchao.guanaitong.constant.MyErrorEnum;

/**
 * 服务异常类
 * @author clark
 * @since 2020-05-14
 */
public class MyException extends RuntimeException {

    public MyException(MyErrorEnum myErrorEnum){

        super(myErrorEnum.buildCodeMsg());
    }

    public MyException(MyErrorEnum myErrorEnum,String msg){

        super(myErrorEnum.buildCodeMsg(msg));
    }
}

