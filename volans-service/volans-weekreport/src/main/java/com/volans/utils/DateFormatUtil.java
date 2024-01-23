package com.volans.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.ZoneId;

public class DateFormatUtil
{
    public static String transformDateFormatToString(Date date, String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date transformDateFormatToDate(Date date, String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String format1 = dateFormat.format(date);
        try
        {
            return dateFormat.parse(format1);
        }
        catch (Exception e)
        {
            return date;
        }
    }

    public static Date transformStringFormatToDate(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return dateFormat.parse(date);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
