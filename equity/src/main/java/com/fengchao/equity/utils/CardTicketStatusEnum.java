package com.fengchao.equity.utils;

import com.fengchao.equity.exception.EquityException;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum CardTicketStatusEnum {
    /**/
    CREATED(1, "未激活"),
    ACTIVE(2, "激活"),
    BOUND(3, "已绑定"),
    EXCHANGED(4, "已兑换"),
    OCCUPIED(5, "已占用"),
    USED(6, "已使用"),
    TIMEOUT(7, "已过期"),
    ASSIGNED(8, "已分配"),
    ;

    private int code;
    private String msg;

    CardTicketStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean canNotTimeout(int code){
        return !(ACTIVE.getCode() == code || BOUND.getCode()==code || EXCHANGED.getCode() == code);
    }

    /*
    public static int String2Int(String status) {
        if (null != status && !status.isEmpty()) {
            int i;
            for (i = 0; i < CardTicketStatusEnum.values().length; i++) {
                if (CardTicketStatusEnum.values()[i].toString().equals(status)) {
                    return CardTicketStatusEnum.values()[i].getCode();
                }
            }
        }
        return 0;
    }
*/
    public static String Int2String(int code) {
        if (0 != code) {
            int i;
            for (i = 0; i < CardTicketStatusEnum.values().length; i++) {
                if (CardTicketStatusEnum.values()[i].getCode() == code) {
                    return CardTicketStatusEnum.values()[i].toString();
                }
            }
        }
        return "";
    }
    public static String int2msg(int code){
        if (0 != code) {
            int i;
            for (i = 0; i < CardTicketStatusEnum.values().length; i++) {
                if (CardTicketStatusEnum.values()[i].getCode() == code) {
                    return CardTicketStatusEnum.values()[i].getMsg();
                }
            }
        }
        return "";
    }

    public static
    Integer normalizeCode(Integer code){
        boolean isRightValue = false;
        if (null != code) {
            for (CardTicketStatusEnum item : CardTicketStatusEnum.values()) {
                if (item.getCode() == code) {
                    isRightValue = true;
                    break;
                }
            }
        }
        if (isRightValue){
            return code;
        }else{
            throw new EquityException(MyErrorEnum.PARAM_TICKET_STATUS_WRONG);
        }
    }
}
