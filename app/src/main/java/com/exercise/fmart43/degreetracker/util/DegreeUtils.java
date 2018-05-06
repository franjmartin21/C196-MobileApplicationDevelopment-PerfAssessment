package com.exercise.fmart43.degreetracker.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.exercise.fmart43.degreetracker.activities.AddTermActivity;
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

    public static Date addHours(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hours);
        return c.getTime();
    }

    public static void openDateDialog(Context context, final View editText){
        final EditText editText1 = (EditText)editText;
        final Calendar calendarToday = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText1.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, calendarToday.get(Calendar.YEAR), calendarToday.get(Calendar.MONTH),calendarToday.get(Calendar.DAY_OF_MONTH)).show();
    }
}
