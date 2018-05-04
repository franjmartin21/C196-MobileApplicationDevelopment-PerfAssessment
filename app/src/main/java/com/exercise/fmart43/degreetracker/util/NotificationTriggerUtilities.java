package com.exercise.fmart43.degreetracker.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.exercise.fmart43.degreetracker.services.DegreeReminderFirebaseJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class NotificationTriggerUtilities {

    private static final int REMINDER_INTERVAL_MINUTES = 60; // todo: set 60
    private static final int REMINDER_INTERVAL_SECONDS = 5;//(int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_MINUTES = 15; //todo: set 15
    private static final int SYNC_FLEXTIME_SECONDS = 5;//(int) (TimeUnit.MINUTES.toSeconds(SYNC_FLEXTIME_MINUTES));

    private static final String REMINDER_JOB_TAG = "degree_reminder_tag";

    private static boolean sInitialized;

    public synchronized static void degreeReminder(@NonNull final Context context){
        if(sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(DegreeReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                //.setRecurring(true)
                .setTrigger(Trigger.NOW)
                //.setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(constraintReminderJob);
        sInitialized = true;


    }
}
