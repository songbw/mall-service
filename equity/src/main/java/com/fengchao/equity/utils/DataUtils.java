package com.fengchao.equity.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public static boolean isValidDate(String s){
        try {
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy-MM-dd其中MM为大写
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2004/02/29会被接受，并转换成2004/03/01
            dateFormat.setLenient(false);
            dateFormat.parse(s);
            return true;
        }catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static Date dateFormat(String strData) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            if(StringUtils.isEmpty(strData)){
                return null;
            }
            data = format.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

}
