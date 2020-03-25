package com.fengchao.product.aoyi.utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @Author tom
 * @Date 19-9-24 下午8:41
 */
public class PriceUtil {

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

    public static Integer random100() {
        Random random = new Random();
        int number = random.nextInt(100);

        return number;
    }


    public static void main(String args[]) {

    }
}
