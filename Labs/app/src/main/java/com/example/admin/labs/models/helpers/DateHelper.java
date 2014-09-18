package com.example.admin.labs.models.helpers;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by admin on 18.09.2014.
 */
public class DateHelper {

    protected static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static Date stringToDate(String dateString){
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date){
        return dateFormat.format(date);
    }
}
