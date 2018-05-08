package com.exercise.fmart43.degreetracker.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeDBHelper;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTermActivity extends AppCompatActivity {

    public enum IntentExtra {
        MODE_EDITION,
        TERM_ID
    }

    public enum Mode{
        ADDITION("Add Term"),
        EDITION("Edit Term");

        String title;

        Mode(String title){
            this.title = title;
        }

        String getTitle(){
            return title;
        }
    }

    private static final int MIN_MONTHS_LENGHT_TERM = 3;
    private static final int MAX_MONTHS_LENGHT_TERM = 9;

    private EditText mTermTitle;

    private EditText mStartDate;

    private EditText mEndDate;

    private View mLayout;

    private DegreeService degreeService;

    private Mode mode;

    private int termIdSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setModePage(Mode.ADDITION);
        degreeService = new DegreeService(this);
        setContentView(R.layout.activity_add_term);

        mLayout = findViewById(R.id.layout_addterm);
        mTermTitle = findViewById(R.id.et_term_title);
        mStartDate = findViewById(R.id.et_start_date);
        mEndDate = findViewById(R.id.et_end_date);

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DegreeUtils.openDateDialog(AddTermActivity.this, view);
            }
        });
        mStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) DegreeUtils.openDateDialog(AddTermActivity.this, view);
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DegreeUtils.openDateDialog(AddTermActivity.this, view);
            }
        });
        mEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) DegreeUtils.openDateDialog(AddTermActivity.this, view);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        if(intent.hasExtra(IntentExtra.MODE_EDITION.name()) && intent.hasExtra(IntentExtra.TERM_ID.name()) ){
            setModePage(Mode.EDITION);
            termIdSelected = intent.getIntExtra(DetailTermActivity.IntentExtra.TERM_ID.name(), 0);
            informTermDetail(degreeService.getTermById(termIdSelected));
        }
    }

    private void setModePage(Mode mode){
        this.mode = mode;
        setTitle(this.mode.getTitle());
    }

    private void informTermDetail(Cursor cursor){
        if(cursor.moveToFirst()){
            mTermTitle.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_TITLE)));
            Date startDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_START_DATE)));
            Date endDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_END_DATE)));

            String startDateStr = DegreeUtils.getStringFromDate(startDate);
            String endDateStr = DegreeUtils.getStringFromDate(endDate);

            mStartDate.setText(startDateStr);
            mEndDate.setText(endDateStr);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent(AddTermActivity.this, MainActivity.class);
            switch (item.getItemId()) {
                case R.id.return_list_term:
                    startActivity(intent);
                    return true;
                case R.id.save_term:
                    boolean saved = saveTerm();
                    if(saved){
                        startActivity(intent);
                    }
                    return true;
            }
            return false;
        }
    };

    private boolean saveTerm(){
        String messageError = validateFields();
        long rows = 0;
        if(messageError == null || messageError.isEmpty()) {
            if(mode == Mode.EDITION)
                rows = degreeService.updateTerm(mTermTitle.getText().toString(), DegreeUtils.getDateFromStr(mStartDate.getText().toString()), DegreeUtils.getDateFromStr(mEndDate.getText().toString()), termIdSelected);
            else
                rows = degreeService.insertTerm(mTermTitle.getText().toString(), DegreeUtils.getDateFromStr(mStartDate.getText().toString()), DegreeUtils.getDateFromStr(mEndDate.getText().toString()));
        }else{
            Snackbar.make(mLayout, messageError, Snackbar.LENGTH_LONG).show();
        }

        if(rows > 0) return true;

        return false;
    }

    private String validateFields(){
        StringBuilder messageError=new StringBuilder();
        String title = mTermTitle.getText().toString();
        String startDateStr = mStartDate.getText().toString();
        String endDateStr = mEndDate.getText().toString();
        if(title == null || title.isEmpty()) messageError.append(getString(R.string.addTerm_title_notinformed)).append("\n");

        if(startDateStr == null || startDateStr.isEmpty()) messageError.append(getString(R.string.addTerm_startDate_notinformed)).append("\n");

        if(endDateStr == null|| endDateStr.isEmpty()) messageError.append(getString(R.string.addTerm_startDate_notinformed)).append("\n");

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = DegreeUtils.DATE_FORMAT_EDIT_TEXT.parse(startDateStr);
            endDate = DegreeUtils.DATE_FORMAT_EDIT_TEXT.parse(endDateStr);
        } catch(ParseException e){
            messageError.append(getString(R.string.addTerm_date_formatnotcorrect));
        }

        if(messageError.length() == 0) {
            if (startDate.after(DegreeUtils.addMonths(endDate, -MIN_MONTHS_LENGHT_TERM))) {
                messageError.append(getString(R.string.addTerm_date_mintermlenght));
            }
            if (!startDate.after(DegreeUtils.addMonths(endDate, -MAX_MONTHS_LENGHT_TERM))) {
                messageError.append(getString(R.string.addTerm_date_maxtermlenght));
            }
        }
        if(messageError.length() == 0 && mode == Mode.ADDITION) {
            Cursor cursor = degreeService.getTermsPriorDate(startDate);
            boolean found = cursor.moveToFirst();

            if(found) messageError.append(getString(R.string.addTerm_termoverlap));
            cursor.close();
        }
        return messageError.toString();
    }

}
