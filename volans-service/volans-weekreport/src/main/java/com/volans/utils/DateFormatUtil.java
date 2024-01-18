package com.volans.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
