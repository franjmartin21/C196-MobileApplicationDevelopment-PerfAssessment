package com.exercise.fmart43.degreetracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity {

    enum IntentExtra{
        MODE_EDITION,
        COURSE_ID
    }

    public enum Mode{
        ADDITION("Add Course"),
        EDITION("Edit Course");

        String title;

        Mode(String title){
            this.title = title;
        }

        String getTitle(){
            return title;
        }
    }

    private View mLayout;

    private EditText mTitle;

    private EditText mStartDate;

    private EditText mEndDate;

    private Spinner mStatus;

    private EditText mMentor;

    private EditText mPhonenumber;

    private EditText mEmail;

    private Spinner mTerm;

    private Mode mode;

    private int courseIdSelected;

    private DegreeService degreeService;

    private Map<String, Integer> termMap;

    private ArrayAdapter<String> adapterStatus;

    private ArrayAdapter<String> adapterTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setModePage(Mode.ADDITION);
        degreeService = new DegreeService(this);

        mLayout = findViewById(R.id.add_course_layout);

        mTitle = findViewById(R.id.et_addcourse_title);
        mStartDate = findViewById(R.id.et_addcourse_startdate);
        mEndDate = findViewById(R.id.et_addcourse_enddate);
        mStatus = findViewById(R.id.sp_addcourse_status);
        mMentor = findViewById(R.id.et_addcourse_mentor);
        mPhonenumber = findViewById(R.id.et_addcourse_phone);
        mEmail = findViewById(R.id.et_addcourse_email);
        mTerm = findViewById(R.id.sp_addcourse_term);

        //Informing the spinners
        adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DegreeTrackerContract.CourseEntry.StatusCourse.getStatusArray());
        mStatus.setAdapter(adapterStatus);

        termMap = getListTermsByTermsCursor(degreeService.getTermList());
        adapterTerm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, termMap.keySet().toArray(new String[termMap.size()]));
        mTerm.setAdapter(adapterTerm);

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog(view);
            }
        });
        mStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) openDateDialog(view);
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog(view);
            }
        });
        mEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) openDateDialog(view);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        if(intent.hasExtra(IntentExtra.MODE_EDITION.name()) && intent.hasExtra(IntentExtra.COURSE_ID.name())){
            setModePage(Mode.EDITION);
            courseIdSelected = intent.getIntExtra(IntentExtra.COURSE_ID.name(), 0);
            informCourseDetail(degreeService.getCourseById(courseIdSelected));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent(AddCourseActivity.this, ListCourseActivity.class);
            switch (item.getItemId()) {
                case R.id.return_list_term:
                    startActivity(intent);
                    return true;
                case R.id.save_term:
                    boolean saved = saveCourse();
                    if(saved){
                        startActivity(intent);
                    }
                    return true;
            }
            return false;
        }
    };

    private Map<String, Integer> getListTermsByTermsCursor(Cursor cursor){
        Map<String, Integer> termMap = new HashMap<>();
        while(cursor.moveToNext()){
            String key = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_TITLE));
            Integer value = cursor.getInt(cursor.getColumnIndex(DegreeTrackerContract.TermEntry._ID));
            termMap.put(key, value);
        }
        return termMap;
    }

    private void openDateDialog(final View editText){
        final EditText editText1 = (EditText)editText;
        final Calendar calendarToday = Calendar.getInstance();
        new DatePickerDialog(AddCourseActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText1.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, calendarToday.get(Calendar.YEAR), calendarToday.get(Calendar.MONTH),calendarToday.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void informCourseDetail(Cursor cursor){
        if(cursor.moveToFirst()){
            mTitle.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_TITLE)));
            Date startDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_START_DATE)));
            mStartDate.setText(DegreeUtils.getStringFromDate(startDate));
            Date endDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE)));
            mEndDate.setText(DegreeUtils.getStringFromDate(endDate));
            int statusSpinnerPosition = adapterStatus.getPosition(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_STATUS)));
            mStatus.setSelection(statusSpinnerPosition);
            mMentor.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME)));
            mPhonenumber.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER)));
            mEmail.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS)));
            int termId = cursor.getInt(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID));
            String termTitle = null;
            for(String key: termMap.keySet()){
                if(termMap.get(key) == termId){
                    termTitle = key;
                    break;
                }
            }
            mTerm.setSelection(adapterTerm.getPosition(termTitle));
        }
    }

    private boolean saveCourse(){
        String messageError = validateFields();
        long rows = 0;
        if(messageError == null || messageError.isEmpty()) {
            String selectedStr = (String) mTerm.getSelectedItem();
            String title = mTitle.getText().toString() ;
            Date startDate = DegreeUtils.getDateFromStr(mStartDate.getText().toString());
            Date endDate = DegreeUtils.getDateFromStr(mEndDate.getText().toString());
            String status = (String)mStatus.getSelectedItem();
            String mentor = mMentor.getText().toString();
            String phoneNumber = mPhonenumber.getText().toString();
            String email = mEmail.getText().toString();
            int termId = termMap.get(selectedStr);
            if(mode == Mode.EDITION) {
                rows = degreeService.updateCourse(title, startDate, endDate, status, mentor, phoneNumber, email, termId, courseIdSelected);
            }
            else
                rows = degreeService.insertCourse(title, startDate, endDate, status, mentor, phoneNumber, email, termId);
        }else{
            Snackbar.make(mLayout, messageError, Snackbar.LENGTH_LONG).show();
        }

        if(rows > 0) return true;

        return false;
    }

    private String validateFields(){

        StringBuilder messageError=new StringBuilder();
        String title = mTitle.getText().toString();
        if(title == null || title.isEmpty()) messageError.append(getString(R.string.addCourse_title_notinformed)).append("\n");

        return messageError.toString();
    }

    private void setModePage(Mode mode){
        this.mode = mode;
        setTitle(this.mode.getTitle());
    }

}
