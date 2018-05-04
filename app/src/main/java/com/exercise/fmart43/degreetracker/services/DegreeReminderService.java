package com.exercise.fmart43.degreetracker.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.exercise.fmart43.degreetracker.util.ReminderTasks;

public class DegreeReminderService extends IntentService{

    public DegreeReminderService() {
        super(DegreeReminderService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
