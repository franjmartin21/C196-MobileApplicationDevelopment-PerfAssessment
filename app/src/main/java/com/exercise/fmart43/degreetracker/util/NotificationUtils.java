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

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.activities.DetailAssessmentActivity;
import com.exercise.fmart43.degreetracker.activities.DetailCourseActivity;

public class NotificationUtils {

    private enum TypeNotification{
        COURSE_GOAL_DATE(1112, 2222, "reminder_notification_channel", R.string.course_notification_title, R.string.course_notification_text),
        ASSESSMENT_GOAL_DATE(1113, 2223, "reminder_notification_channel", R.string.assessment_notification_title, R.string.assessment_notification_text);

        int notificationId;

        int intentId;

        String channelId;

        int contentTitleResourceId;

        int contentTextResourceId;

        TypeNotification(int notificationId, int intentId, String channelId, int contentTitleResourceId, int contentTextResourceId){
            this.notificationId = notificationId;
            this.intentId = intentId;
            this.channelId = channelId;
            this.contentTitleResourceId = contentTitleResourceId;
            this.contentTextResourceId = contentTextResourceId;
;
        }
    }

    public static void sendNotificationAssessment(Context context, int courseId, String courseTitle, int assessmentId){
        Intent startActivityIntent = new Intent(context, DetailAssessmentActivity.class);
        startActivityIntent.putExtra(DetailAssessmentActivity.IntentExtra.ASSESSMENT_ID.name(), assessmentId);
        startActivityIntent.putExtra(DetailAssessmentActivity.IntentExtra.COURSE_ID.name(), courseId);
        sendNotification(context, TypeNotification.ASSESSMENT_GOAL_DATE, courseTitle, startActivityIntent );
    }

    public static void sendNotificationCourse(Context context, int courseId, String courseTitle){
        Intent startActivityIntent = new Intent(context, DetailCourseActivity.class);
        startActivityIntent.putExtra(DetailCourseActivity.IntentExtra.COURSE_ID.name(), courseId);
        sendNotification(context, TypeNotification.COURSE_GOAL_DATE, courseTitle, startActivityIntent);
    }

    private static void sendNotification(Context context, TypeNotification typeNotification, String courseTitle, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(typeNotification.channelId,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,typeNotification.channelId)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_assessment_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(typeNotification.contentTitleResourceId))
                .setContentText(context.getString(typeNotification.contentTextResourceId, courseTitle))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(getPendingIntent(context, intent, typeNotification))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(typeNotification.notificationId, notificationBuilder.build());
    }

    private static PendingIntent getPendingIntent(Context context, Intent intent, TypeNotification typeNotification){
        return PendingIntent.getActivity(context,
                typeNotification.intentId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIcon(Context context){
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_assessment_24dp);
        return largeIcon;
    }
}
