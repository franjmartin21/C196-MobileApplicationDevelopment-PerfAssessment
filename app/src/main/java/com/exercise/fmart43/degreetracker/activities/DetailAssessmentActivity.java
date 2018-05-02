package com.exercise.fmart43.degreetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;

public class DetailAssessmentActivity extends AppCompatActivity {

    public enum IntentExtra {
        COURSE_ID,
        ASSESSMENT_ID
    }
    private DegreeService degreeService;

    private View mLayout;

    private BottomNavigationView mNavigation;

    private EditText mTitle;

    private EditText mDate;

    private Spinner mType;

    private ArrayAdapter<String> adapterStatus;

    private int assessmentId;

    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        degreeService = new DegreeService(this);
        setContentView(R.layout.activity_detail_assessment);

        mLayout = findViewById(R.id.layout_detail_assessment);

        mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTitle = findViewById(R.id.et_assessment_title);
        mDate = findViewById(R.id.et_assessment_date);
        mType = findViewById(R.id.sp_assessment_type);

        adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DegreeTrackerContract.AssessmentEntry.TypeAssessment.getStatusArray());
        mType.setAdapter(adapterStatus);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DegreeUtils.openDateDialog(DetailAssessmentActivity.this, view);
            }
        });

        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) DegreeUtils.openDateDialog(DetailAssessmentActivity.this, view);
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra(DetailNoteActivity.IntentExtra.COURSE_ID.name())){
            courseId = intent.getIntExtra(DetailNoteActivity.IntentExtra.COURSE_ID.name(), 0);
        }

        if(intent.hasExtra(IntentExtra.ASSESSMENT_ID.name())){
            assessmentId = intent.getIntExtra(IntentExtra.ASSESSMENT_ID.name(), 0);
            informAssessmentData();
        }
    }

    private void informAssessmentData() {
        Cursor cursor = degreeService.getAssessmentById(assessmentId);
        if(cursor.moveToFirst()) {
            int typeSpinnerPosition = adapterStatus.getPosition(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE)));
            mTitle.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE)));
            Date date = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_DATE)));
            mDate.setText(DegreeUtils.getStringFromDate(date));
            mType.setSelection(typeSpinnerPosition);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Intent intent = new Intent(AddTermActivity.this, MainActivity.class);
            switch (item.getItemId()) {
                case R.id.return_list_term:
                    finish();
                    return true;
                case R.id.save_term:
                    saveAssessment();
                    finish();
                    return true;
            }
            return false;
        }
    };

    private String validate(){
        StringBuilder messageError = new StringBuilder();
        String title = mTitle.getText().toString();
        if(title == null || title.isEmpty()) messageError.append(getString(R.string.addTerm_title_notinformed)).append("\n");

        return messageError.toString();
    }

    private void saveAssessment(){
        String messageError = validate();
        if(messageError == null || messageError.isEmpty()) {
            String type = (String)mType.getSelectedItem();
            if (assessmentId > 0) {
                degreeService.updateAssessment(mTitle.getText().toString(), DegreeUtils.getDateFromStr(mDate.getText().toString()), type, courseId, assessmentId);
            } else {
                degreeService.insertAssessment(mTitle.getText().toString(), DegreeUtils.getDateFromStr(mDate.getText().toString()), type, courseId);
            }
        } else {
            Snackbar.make(mLayout, messageError, Snackbar.LENGTH_LONG).show();
        }
    }

}
