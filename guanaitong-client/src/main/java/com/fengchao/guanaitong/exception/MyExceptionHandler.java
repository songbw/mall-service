package com.fengchao.guanaitong.exception;

import com.fengchao.guanaitong.bean.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务异常处理类
 * @author clark
 * @since 2020-05-14
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> errorHandle(Exception e, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        if (log.isDebugEnabled()) {
            log.debug("raw message: " + e.getMessage());
        }
        String message = e.getMessage();
        if (null == message){
            message = e.getCause().getMessage();
            if (log.isDebugEnabled()) {
                log.error("new message: {}", message);
            }
        }

        String errMessage = (null == message ?"exception": message.trim());
        log.error(errMessage);

        int errCode = ResultObject.MY_HTTP_ERROR_STATUS_CODE;
        if (null != message){
            String defMsg = "default message";
            int msgIndex = message.lastIndexOf(defMsg);
            if (0 < msgIndex){
                errMessage = message.substring(msgIndex+defMsg.length()).trim();
                message = errMessage;
            }/*else {
                if (message.contains(":")) {
                    String[] errInfo = message.split(":");
                    errMessage = errInfo[errInfo.length - 1].trim();
                }
            }*/

            int codeIndexBegin = message.indexOf("@");
            int codeIndexEnd = message.indexOf("@",codeIndexBegin+1);
            //log.info("==errMessage= {} , codeBegin={} codeEnd={}",message,codeIndexBegin,codeIndexEnd);
            if (0 <= codeIndexBegin && 1 < codeIndexEnd){
                String codeStr = message.substring(codeIndexBegin+1,codeIndexEnd);
                Integer code;
                try {
                    code = Integer.valueOf(codeStr);
                }catch (Exception ex){
                    log.error("wrong error code {}",codeStr,ex);
                    code = 0;
                }
                if (0 < code){
                    errMessage = message.substring(codeIndexEnd+1);
                    errCode = code;
                }
            }
        }

        if (null != e.getCause()) {
            map.put(ResultObject.DATA,e.getCause().getMessage());
        }
        map.put(ResultObject.MESSAGE,errMessage);
        map.put(ResultObject.CODE,errCode);

        response.setStatus(ResultObject.MY_HTTP_ERROR_STATUS_CODE);
        return map;
    }

}
