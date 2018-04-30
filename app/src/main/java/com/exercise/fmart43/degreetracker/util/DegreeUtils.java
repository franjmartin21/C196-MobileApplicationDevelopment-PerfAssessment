package com.exercise.fmart43.degreetracker.util;

import android.util.Log;

import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DegreeUtils {

    public static final DateFormat DATE_FORMAT_EDIT_TEXT;

    public static final DateFormat DATE_TIME_FORMAT_EDIT_TEXT;

    static {
        DATE_FORMAT_EDIT_TEXT = new SimpleDateFormat("dd/MM/yyyy");
        DATE_TIME_FORMAT_EDIT_TEXT = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

    public static Date getDateFromStr(String dateStr){
        Date date = null;
        try {
            date = DATE_FORMAT_EDIT_TEXT.parse(dateStr);
        } catch (ParseException ex){
            Log.e(DegreeTrackerContract.class.getName(), "Problem parsing value " + dateStr);
        }
        return date;
    }

    public static String getStringFromDate(Date date){
        if(date == null) return null;

        return DATE_FORMAT_EDIT_TEXT.format(date);
    }

    public static String getStringFromDateTime(Date dateTime){
        if(dateTime == null) return null;

        return DATE_TIME_FORMAT_EDIT_TEXT.format(dateTime);
    }

    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }
}
