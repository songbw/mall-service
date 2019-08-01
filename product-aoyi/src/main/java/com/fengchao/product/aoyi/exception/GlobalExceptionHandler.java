package com.fengchao.product.aoyi.exception;

import com.fengchao.product.aoyi.bean.OperaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    OperaResponse handleException(Exception e) {
        log.info(e.getMessage(), e);
        OperaResponse operaResponse = new OperaResponse();
        operaResponse.setMsg("系统参数错误");
        operaResponse.setCode(200500);
        return operaResponse ;
    }


    /**
     * 处理实体字段校验不通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public OperaResponse validationError(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        OperaResponse operaResponse = new OperaResponse();
        operaResponse.setMsg(ex.getMessage());
        operaResponse.setCode(200500);
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder builder = new StringBuilder();
        for (FieldError error : fieldErrors) {
            builder.append( " " + error.getDefaultMessage());
        }
        operaResponse.setMsg(builder.toString());
        return operaResponse ;
    }

}
