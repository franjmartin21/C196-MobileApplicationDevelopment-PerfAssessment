package com.exercise.fmart43.degreetracker.services;


import android.content.Context;
import android.os.AsyncTask;

import com.exercise.fmart43.degreetracker.util.ReminderTasks;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class DegreeReminderFirebaseJobService extends JobService{

    private AsyncTask mBackgroudTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroudTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = DegreeReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroudTask.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackgroudTask != null) mBackgroudTask.cancel(true);
        return true;
    }
}
