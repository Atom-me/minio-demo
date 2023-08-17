package com.atom.minio.utils;


import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @author Atom
 */
public class DateUtil {

    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);


    /**
     * str to date,
     * <p>
     * "2019-02-28 23:53:33", "yyyy-MM-dd HH:mm:ss"
     * "2019:02:28***23:53:33", "yyyy:MM:dd***HH:mm:ss"
     * "2019:02:28*=====**23:53:=-33", "yyyy:MM:dd*=====**HH:mm:=-ss"
     *
     * @param timeStr "2018-06-05 12:23:34" must match param pattern
     * @param pattern "yyyy-MM-dd HH:mm:ss" must match param timeStr
     * @return
     * @author Atom
     */
    public static Date strToDate(String timeStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    /**
     * @param datetime "2017-05-06 23:45:33"
     * @param minutes  15
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static String addMinutesToString(String datetime, int minutes) {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(datetime, DEFAULT_DATETIME_FORMATTER).plusMinutes(minutes);
        return dateTime.format(DEFAULT_DATETIME_FORMATTER);
    }


    /**
     * @param datetime "2017-05-06 23:45:33"
     * @param minutes  15
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static Date addMinutesToDate(String datetime, int minutes) {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(datetime, DEFAULT_DATETIME_FORMATTER).plusMinutes(minutes);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 获取几天后的当前时间
     *
     * @param days
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static Date addDaysFromNow(int days) {
        return addDaysFromSpecialDate(new Date(), days);
    }

    /**
     * 获取从当前时间开始，几分钟后的时间
     *
     * @param minutes
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static Date addMinutesFromNow(int minutes) {
        return addMinutesFromSpecDate(new Date(), minutes);
    }

    /**
     * 获取从指定时间开始，几分钟后的时间
     *
     * @param minutes
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static Date addMinutesFromSpecDate(Date date, int minutes) {
        if (Objects.isNull(date)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusMinutes(minutes);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * @param datetime "2017-05-06 23:45:33"
     * @param days     15
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static Date addDaysToDate(String datetime, int days) {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(datetime, DEFAULT_DATETIME_FORMATTER).plusDays(days);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    /**
     * 给指定的时间添加天数
     * <p>
     * 返回字符串格式：yyyy-MM-dd HH:mm:ss
     *
     * @param datetime "2017-05-06 23:45:33"
     * @param days     15
     * @return 返回字符串格式：yyyy-MM-dd HH:mm:ss 2017-05-07 00:00:33
     * @author Atom
     */
    public static String addDaysFromSpecialDate(String datetime, int days) {
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.parse(datetime, DEFAULT_DATETIME_FORMATTER).plusDays(days);
        return dateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 给指定的时间添加天数
     * 返回 {@link Date} 对象
     *
     * @param days 要增加的天数
     * @return Date
     * @author Atom
     */
    public static Date addDaysFromSpecialDate(Date date, int days) {
        if (Objects.isNull(date)) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(days);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * @param datetime "2017-05-06 23:45:33"
     * @return 2017-05-07 00:00:33
     * @author Atom
     */
    public static String dateToString(Date datetime) {
        if (Objects.isNull(datetime)) {
            return null;
        }
        LocalDateTime newDateTime = LocalDateTime.ofInstant(datetime.toInstant(), ZoneId.systemDefault());
        return DEFAULT_DATETIME_FORMATTER.format(newDateTime);
    }

    /**
     * 获取当天零点（开始时间）
     *
     * @return 2020-09-05 00:00:00
     */
    public static String getTodayStartTimeStr() {
        LocalDateTime todayStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        return todayStartTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 获取当天23:59:59（结束时间）
     *
     * @return 2020-09-05 23:59:59
     */
    public static String getTodayEndTimeStr() {
        LocalDateTime todayStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return todayStartTime.format(DEFAULT_DATETIME_FORMATTER);
    }


    /**
     * LocalDateTime to Format String
     *
     * @param localDateTime
     * @return
     */
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * LocalDate to Date
     *
     * @param localDate
     * @return
     */
    public static Date asDate(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime to Date
     *
     * @param localDateTime
     * @return
     */
    public static Date asDate(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date to LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate asLocalDate(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date to LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * String to LocalDateTime
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime asLocalDateTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        Date date = strToDate(dateStr, DEFAULT_DATETIME_PATTERN);
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * date to str with customize pattern
     * <p>
     * DateUtil.dateToStrWithCustomizePattern(new Date(), "yyyy-MM-dd");  --> 2020-09-17
     * DateUtil.dateToStrWithCustomizePattern(new Date(2019, 10, 15), "yy-MM-dd"); --> 19-11-15
     *
     * @param date
     * @param pattern only support date ,not support date with time,such as "yyyy-MM-dd", not "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String formatDateWithCustomizePattern(Date date, String pattern) {
        return asLocalDate(date).format(DateTimeFormatter.ofPattern(pattern));
    }
}
