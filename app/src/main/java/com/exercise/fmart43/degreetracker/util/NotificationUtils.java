package com.exercise.fmart43.degreetracker.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.activities.DetailCourseActivity;
import com.exercise.fmart43.degreetracker.services.DegreeReminderService;

public class NotificationUtils {

    private static final int COURSE_GOAL_ENDDATE_COMING_NOTIFICATION_ID = 1121;
    private static final int COURSE_GOAL_ENDDATE_COMING_INTENT_ID = 1122;
    private static final String COURSE_GOAL_ENDDATE_COMING_CHANNEL_ID = "reminder_notification_channel";

    private static final int ACTION_DISMISS_NOTIFICATION = 1123;


    public static void remindUserCourseEndDateComing(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(COURSE_GOAL_ENDDATE_COMING_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,COURSE_GOAL_ENDDATE_COMING_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_assessment_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Course ending")
                .setContentText("The course is close to eeeeeeeeeend")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("The course is close to eeeeeeeeeend"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .addAction(dismissNotification(context));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(COURSE_GOAL_ENDDATE_COMING_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, DetailCourseActivity.class);
        return PendingIntent.getActivity(context,
                COURSE_GOAL_ENDDATE_COMING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                );
    }

    private static Bitmap largeIcon(Context context){
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_assessment_24dp);
        return largeIcon;
    }

    public static NotificationCompat.Action dismissNotification(Context context){
        Intent dismissNotificationIntent = new Intent(context, DegreeReminderService.class);
        dismissNotificationIntent.setAction(ReminderTasks.DISMISS_NOTIFICATION);
        PendingIntent dismissNotificationPendingIntent = PendingIntent.getService(context, ACTION_DISMISS_NOTIFICATION, dismissNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismissNotifiactionAction = new NotificationCompat.Action(R.drawable.ic_add_white_24dp,"Dismiss", dismissNotificationPendingIntent);
        return dismissNotifiactionAction;
    }

    public static NotificationCompat.Action keepReminding(Context context){
        return null;
    }

}
