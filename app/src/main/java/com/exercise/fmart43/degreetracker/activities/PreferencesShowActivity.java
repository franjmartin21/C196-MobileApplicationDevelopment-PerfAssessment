package com.exercise.fmart43.degreetracker.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.util.NotificationUtils;

//todo: clase a eliminar
public class PreferencesShowActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mPref1;

    private TextView mPref2;

    private TextView mPref3;

    private TextView mPref4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_show);
        mPref1 = findViewById(R.id.tv_pref1);
        mPref2 = findViewById(R.id.tv_pref2);
        mPref3 = findViewById(R.id.tv_pref3);
        mPref4 = findViewById(R.id.tv_pref4);
        setupSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPref1.setText(String.valueOf(sharedPreferences.getBoolean(getString(R.string.preference_enable_notification_course_key),
                getResources().getBoolean(R.bool.preference_enable_notification_course_default))));

        mPref2.setText(String.valueOf(sharedPreferences.getString(getString(R.string.preference_time_notification_course_key),
                getResources().getString(R.string.preference_time_notification_course_value_default))));

        mPref3.setText(String.valueOf(sharedPreferences.getBoolean(getString(R.string.preference_enable_notification_assessment_key),
                getResources().getBoolean(R.bool.preference_enable_notification_assessment_default))));

        mPref4.setText(String.valueOf(sharedPreferences.getString(getString(R.string.preference_time_notification_assessment_key),
                getResources().getString(R.string.preference_time_notification_assessment_value_default))));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.preference_enable_notification_course_key))){
            mPref1.setText(String.valueOf(sharedPreferences.getBoolean(getString(R.string.preference_enable_notification_course_key),
                    getResources().getBoolean(R.bool.preference_enable_notification_course_default))));
        }
        else if(key.equals(getString(R.string.preference_time_notification_course_key))) {
            mPref2.setText(String.valueOf(sharedPreferences.getString(getString(R.string.preference_time_notification_course_key),
                    getResources().getString(R.string.preference_time_notification_course_value_default))));
        }
        else if(key.equals(getString(R.string.preference_enable_notification_assessment_key))) {
            mPref3.setText(String.valueOf(sharedPreferences.getBoolean(getString(R.string.preference_enable_notification_assessment_key),
                    getResources().getBoolean(R.bool.preference_enable_notification_assessment_default))));
        }
        else if(key.equals(getString(R.string.preference_time_notification_course_key))) {
            mPref4.setText(String.valueOf(sharedPreferences.getString(getString(R.string.preference_time_notification_assessment_key),
                    getResources().getString(R.string.preference_time_notification_assessment_value_default))));
        }

    }

    public void handleClick(View view) {
        NotificationUtils.remindUserCourseEndDateComing(this);
    }
}
