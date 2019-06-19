package com.fengchao.pingan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String dateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = format.format(new Date());
        return dateString;
    }

}
