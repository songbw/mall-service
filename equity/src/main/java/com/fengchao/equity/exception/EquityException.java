package com.fengchao.equity.exception;

import com.fengchao.equity.utils.MyErrorEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * @author fengchao
 */

@Getter
@Setter
@ControllerAdvice
public class EquityException extends RuntimeException {

    private String module = "Equity";
    private Integer code = 500;
    private String msg = "内部异常";

    public EquityException() {
        super();
    }

    public EquityException(MyErrorEnum errorEnum){
        //super(errorEnum.getCode()+":"+errorEnum.getMsg());
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    public EquityException(Integer code, String msg) {
        //super(code+":"+msg);
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
