package com.fengchao.order.utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class CalculateUtil {

    /**
     * 元 转 分
     *
     * @param yuan
     * @return
     */
    public static Integer convertYuanToFen(String yuan) {
        if (StringUtils.isBlank(yuan)) {
            return null;
        }

        int fen = new BigDecimal(yuan).multiply(new BigDecimal(100)).intValue();

        return fen;
    }

    /**
     * 分 转 元
     *
     * @param fen
     * @return
     */
    public static String converFenToYuan(Integer fen) {
        if (fen == null) {
            return null;
        }

        String yuan = new BigDecimal(fen).divide(new BigDecimal(100)).toString();

        return yuan;
    }

}