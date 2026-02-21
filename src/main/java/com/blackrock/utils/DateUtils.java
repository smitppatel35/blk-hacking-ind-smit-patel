package com.blackrock.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    public static String format(LocalDateTime date) {
        return date.format(FORMATTER);
    }
}