package com.exercise.fmart43.degreetracker.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.NotificationUtils;

import java.util.Date;

public class DegreeNotificationService {

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
        Cursor cursor = degreeService.getCourseList();
        int cursorCount = cursor.getCount();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean preferenceEnabled = sharedPreferences.getBoolean(context.getString(R.string.preference_enable_notification_course_key),
                context.getResources().getBoolean(R.bool.preference_enable_notification_course_default));
        /*
        for(int i = 0; i < cursorCount; i++){
            cursor.move(i);
            Date endDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE)));
        }*/
        if(preferenceEnabled) NotificationUtils.sendNotificationCourse(context, 1, "Mobile...");
    }

    private void checkForAssessmentNotifications(){
        NotificationUtils.sendNotificationAssessment(context, 1, "Algorithms", 1);
    }
}
