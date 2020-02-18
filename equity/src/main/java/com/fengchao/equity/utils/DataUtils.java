package com.fengchao.equity.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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

    public static String decimalFormat(String price){

        double priceNum = Double.parseDouble(price)/100;
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(priceNum);
        return format;
    }

    public static boolean isContainDate(Date s1, Date e1,Date s2, Date e2){

//        if((s1.before(s2)) && (e1 > s2)){
//            System.out.println("有交集");
//        }else if((s1 > s2)&&(s1 < e2)){
//            System.out.println("有交集");
//        }else{
//            System.out.println("无交集");
//        }
        if((s1.equals(s2))&&(e1.equals(e2))){
            return true;
        }else if((s1.before(s2)) && (e1.after(s2))){
            return true;
        }else if((s1.after(s2))&&(s1.before(e2))){
            return true;
        }else{
           return false;
        }
    }

    public static boolean isSameDay(Date s1, Date s2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(s1);
        c2.setTime(s2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static Date getFetureDate(Date date, int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.DATE, past);
        Date today = calendar.getTime();
        return today;
    }

    /**
     * 将Date类型的日期按照format转换
     *
     * @param date
     * @return
     */
    public static String dateTimeFormat(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        String dateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return dateTime;
    }

    public static void main(String[] args) {
        System.out.println(decimalFormat("0.01"));
    }
}
