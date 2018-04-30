package com.exercise.fmart43.degreetracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.exercise.fmart43.degreetracker.R;

public class DetailAssessmentActivity extends AppCompatActivity {

    public enum IntentExtra {
        COURSE_ID,
        ASSESSMENT_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_assessment);
    }
}
