package com.exercise.fmart43.degreetracker.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;
import com.exercise.fmart43.degreetracker.util.NotificationUtils;

import java.util.Date;

public class DegreeNotificationService {

    private enum PreferenceValue{
        hour(1),
        day(24),
        week(168);

        int hours;

        PreferenceValue(int hours){
            this.hours = hours;
        }
    }

    private Context context;

    private DegreeService degreeService;

    public DegreeNotificationService(Context context){
        this.context = context;
        this.degreeService = new DegreeService(context);
    }

    public void checkForNotifications(){
        checkForCourseNotifications();
        checkForAssessmentNotifications();
    }

    private void checkForCourseNotifications(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean preferenceEnabled = sharedPreferences.getBoolean(context.getString(R.string.preference_enable_notification_course_key),
                context.getResources().getBoolean(R.bool.preference_enable_notification_course_default));

        String preferenceTime = sharedPreferences.getString(context.getString(R.string.preference_time_notification_course_key),
                context.getString(R.string.preference_time_notification_course_value_default));

        int preferenceTimeValue = PreferenceValue.valueOf(preferenceTime).hours;
        Cursor cursor = degreeService.getCourseList();
        int courseForNotification = getCourseForNotification(cursor, preferenceTimeValue);

        if(preferenceEnabled && courseForNotification > 0) {
            cursor = degreeService.getCourseById(courseForNotification);
            cursor.moveToFirst();
            String courseTitle = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_TITLE));
            NotificationUtils.sendNotificationCourse(context, courseForNotification, courseTitle);
        }
    }

    private void checkForAssessmentNotifications(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean preferenceEnabled = sharedPreferences.getBoolean(context.getString(R.string.preference_enable_notification_assessment_key),
                context.getResources().getBoolean(R.bool.preference_enable_notification_assessment_default));

        String preferenceTime = sharedPreferences.getString(context.getString(R.string.preference_time_notification_assessment_key),
                context.getString(R.string.preference_time_notification_assessment_value_default));

        int preferenceTimeValue = PreferenceValue.valueOf(preferenceTime).hours;
        Cursor cursor = degreeService.getAssessmentList();
        int assessmentForNotification = getAssessmentForNotification(cursor, preferenceTimeValue);

        if(preferenceEnabled && assessmentForNotification > 0) {
            cursor = degreeService.getAssessmentById(assessmentForNotification);
            cursor.moveToFirst();
            String courseTitle = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.LABEL_COURSE_TITLE));
            int courseId = cursor.getInt(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID));
            NotificationUtils.sendNotificationAssessment(context, courseId, courseTitle, assessmentForNotification);
        }
    }

    private int getCourseForNotification(Cursor cursor, int hoursRangeShow){
        Date now = new Date();
        int courseForNotification = -1;
        while(cursor.moveToNext()){
            Date date = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE)));
            DegreeTrackerContract.CourseEntry.StatusCourse courseStatus = DegreeTrackerContract.CourseEntry.StatusCourse.valueOf(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_STATUS)));
            int courseId = cursor.getInt(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry._ID));
            if(courseStatus == DegreeTrackerContract.CourseEntry.StatusCourse.IN_PROGRESS && DegreeUtils.addHours(now, hoursRangeShow).after(date)){
                courseForNotification = courseId;
                break;
            }
        }
        return courseForNotification;
    }

    private int getAssessmentForNotification(Cursor cursor, int hoursRangeShow){
        int assessmentForNotification = -1;
        Date now = new Date();
        while(cursor.moveToNext()){
            Date date = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_DATE)));
            int assessmentId = cursor.getInt(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry._ID));
            if(now.before(date) && DegreeUtils.addHours(now, hoursRangeShow).after(date)){
                assessmentForNotification = assessmentId;
                break;
            }
        }
        return assessmentForNotification;
    }
}
