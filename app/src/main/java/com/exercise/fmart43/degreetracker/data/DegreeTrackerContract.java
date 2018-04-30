package com.exercise.fmart43.degreetracker.data;

import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DegreeTrackerContract {

    public static final DateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DB_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static Date getDateFromDBStrValue(String fieldValue){
        Date date = null;
        if(fieldValue == null) return null;

        try {
            date = DB_DATE_FORMAT.parse(fieldValue);
        } catch (ParseException ex){
            Log.e(DegreeTrackerContract.class.getName(), "Problem parsing value " + fieldValue);
        }
        return date;
    }

    public static Date getDateTimeFromDBStrValue(String fieldValue){
        Date date = null;
        if(fieldValue == null) return null;

        try {
            date = DB_DATETIME_FORMAT.parse(fieldValue);
        } catch (ParseException ex){
            Log.e(DegreeTrackerContract.class.getName(), "Problem parsing value " + fieldValue);
        }
        return date;
    }

    public static String getStringForDatabase(Date fieldDate){
        if(fieldDate == null) return null;

        return DB_DATE_FORMAT.format(fieldDate);
    }

    public static final class TermEntry implements BaseColumns{
        public enum StatusTerm{
            PENDING("PENDING"),
            IN_PROGRESS("IN PROGRESS"),
            COMPLETED("COMPLETED");
            String title;

            StatusTerm(String title){
                this.title = title;
            }

            public String getTitle() {
                return title;
            }
        }

        public static final int NUM_INITIAL_TERMS = 2;

        public static final String TABLE_NAME = "Term";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_START_DATE = "startDate";

        public static final String COLUMN_END_DATE = "endDate";

        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static String getStatusByStartEndDate(Date startDate, Date endDate){
            String status = null;
            if(startDate == null || startDate == null) return DegreeTrackerContract.TermEntry.StatusTerm.PENDING.getTitle();

            Date today = new Date();
            if(startDate.after(today)) status = DegreeTrackerContract.TermEntry.StatusTerm.COMPLETED.getTitle();

            if(startDate.before(today) && endDate.after(today)) status = DegreeTrackerContract.TermEntry.StatusTerm.IN_PROGRESS.getTitle();

            else status = DegreeTrackerContract.TermEntry.StatusTerm.PENDING.getTitle();

            return status;
        }
    }

    public static final class CourseEntry implements BaseColumns{

        public static final String TABLE_NAME = "Course";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_START_DATE = "startDate";

        public static final String COLUMN_END_DATE = "endDate";

        public static final String COLUMN_STATUS = "status";

        public static final String COLUMN_MENTOR_NAME = "mentorName";

        public static final String COLUMN_PHONE_NUMBER = "phoneNumber";

        public static final String COLUMN_EMAIL_ADDRESS = "emailAddress";

        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String COLUMN_TERM_ID = "term_Id";

        public static final String LABEL_TERM_TITLE = "term_title";

        public enum StatusCourse{
            PENDING,
            IN_PROGRESS,
            DROPPED,
            FINISHED;

            static public String[] getStatusArray(){
                String[] array = new String[StatusCourse.values().length];
                int i = 0;
                for(StatusCourse statusCourse: StatusCourse.values()){
                    array[i++] = statusCourse.name();
                }
                return array;
            }
        }
    }

    public static final class NoteEntry implements BaseColumns{

        public static final String TABLE_NAME = "Note";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String COLUMN_COURSE_ID = "course_Id";

    }

    public static final class AssessmentEntry implements  BaseColumns{
        public static final String TABLE_NAME = "Assessment";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_TYPE = "type";

        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String COLUMN_COURSE_ID = "course_Id";

        public enum TypeAssessment{
            PREASSESSMENT("O"),
            OBJECTIVE("O"),
            PERFORMANCE("P");

            String prefix;

            TypeAssessment(String prefix){
                this.prefix = prefix;
            }

            public String getPrefix() {
                return prefix;
            }
        }
    }
}
