package com.exercise.fmart43.degreetracker.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ReminderTasks {
    public static final String ACTION_KEEP_REMINDING = "keep-reminding";

    public static final String DISMISS_NOTIFICATION = "dismiss-notification";

    public static final String ACTION_REMINDER = "action-notification-reminder";

    public static void executeTask(Context context, String action){
        if(ACTION_KEEP_REMINDING.equals(action)){
            Log.i(ReminderTasks.class.getSimpleName(), "Working " + action);
        } else if(DISMISS_NOTIFICATION.equals(action)){
            Log.i(ReminderTasks.class.getSimpleName(), "Working " + action);
        } else if(ACTION_REMINDER.equals(action)){
            remindEndCourseGoal(context);
        }
    }

    private static void remindEndCourseGoal(Context context){
        NotificationUtils.remindUserCourseEndDateComing(context);
    }
}
