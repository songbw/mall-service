package com.fengchao.equity.utils;

import org.apache.commons.lang.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static final String DATE_YYYYMMDD = "yyyyMMdd";

    public static final String DATE_F_YYYYMD = "yyyy/M/d";

    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String DATE_YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_YYYY_MM_DD_HH_MM_ss_S = "yyyy-MM-dd HH:mm:ss.S";

    public static final String DATE_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String TIME_HHmm = "HHmm";

    public static final String TIME_HH_mm = "HH:mm";

    public static final String TIME_HH_mm_ss = "HH:mm:ss";

    /**
     * 将一个日期加上或减去 seocndsToAdd 妙数,得出新的日期
     *
     * @param originDate
     * @param originFormat
     * @param seocndsToAdd
     * @param newFormat
     * @return
     */
    public static String calcMinute(String originDate, String originFormat, long seocndsToAdd, String newFormat) {
        String formatDate = "";

        if (StringUtils.isNotBlank(originDate)) {
            // 将原始的日期转换成localDate
            LocalDateTime localDateTime = LocalDateTime.parse(originDate, DateTimeFormatter.ofPattern(originFormat));

            // 计算分钟
            LocalDateTime _localDateTime = localDateTime.plusSeconds(seocndsToAdd);


            // 将计算完的日期转换成需要的格式
            formatDate = _localDateTime.format(DateTimeFormatter.ofPattern(newFormat));
        }

        return formatDate;
    }

    /**
     * 将指定的(日期/时间)格式转换成另一种指定的格式
     *
     * @param dateTime
     * @param originFormat
     * @param newFormat
     * @return
     */
    public static String dateTimeFormat(String dateTime, String originFormat, String newFormat) {
        return dateTimeFormat(dateTime, originFormat, newFormat, 0L);
    }

    /**
     * 将Date类型的日期按照format转换
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateTimeFormat(Date date, String format) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        String dateTime = localDateTime.format(DateTimeFormatter.ofPattern(format));

        return dateTime;
    }

    /**
     * 将日期加上 diffTime 秒数 并 将指定的(日期/时间)格式转换成另一种指定的格式
     *
     *
     * @param dateTime
     * @param originFormat
     * @param newFormat
     * @param diffTime 间隔时间，单位：秒
     * @return
     */
    public static String dateTimeFormat(String dateTime, String originFormat, String newFormat, long diffTime) {
        String formatDateTime = "";

        if (StringUtils.isNotBlank(dateTime)) {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(originFormat));
            formatDateTime = localDateTime.plusSeconds(diffTime).format(DateTimeFormatter.ofPattern(newFormat));
        }

        return formatDateTime;
    }

    /**
     * 将指定的(日期)格式转换成另一种指定的格式
     *
     * @param date
     * @param originFormat
     * @param newFormat
     * @return
     */
    public static String dateFormat(String date, String originFormat, String newFormat) {
        String formatDate = "";

        if (StringUtils.isNotBlank(date)) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(originFormat));
            formatDate = localDate.format(DateTimeFormatter.ofPattern(newFormat));
        }

        return formatDate;
    }

    /**
     * 获取当前的日期时间
     *
     * @param pattern
     * @return
     */
    public static String nowDateTime(String pattern) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

        return now;
    }

    /**
     * 获取当前的日期
     *
     * @param pattern
     * @return
     */
    public static String nowDate(String pattern) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

        return now;
    }

    /**
     * 计算 endDateTime - startDateTime 的分钟差
     *
     * @param startDateTime
     * @param startDateTimeFormat
     * @param endDateTime
     * @param endDateTimeFormat
     * @return
     */
    public static Long diffMinutes(String startDateTime, String startDateTimeFormat,
                               String endDateTime, String endDateTimeFormat) {
        LocalDateTime start = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern(startDateTimeFormat));
        LocalDateTime end = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern(endDateTimeFormat));

        Duration duration = Duration.between(start, end);
        Long diff = duration.toMinutes();

        return diff;
    }

    /**
     * 计算 endDateTime - startDateTime 的小时差
     *
     * @param startDateTime
     * @param startDateTimeFormat
     * @param endDateTime
     * @param endDateTimeFormat
     * @return
     */
    public static Long diffHours(String startDateTime, String startDateTimeFormat,
                                 String endDateTime, String endDateTimeFormat) {
        LocalDateTime start = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern(startDateTimeFormat));
        LocalDateTime end = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern(endDateTimeFormat));

        Duration duration = Duration.between(start, end);
        Long diff = duration.toHours();

        return diff;
    }


}
