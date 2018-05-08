package com.exercise.fmart43.degreetracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.adapters.CourseAdapter;
import com.exercise.fmart43.degreetracker.adapters.TermAdapter;
import com.exercise.fmart43.degreetracker.data.DegreeDBHelper;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;

public class DetailTermActivity extends AppCompatActivity implements CourseAdapter.ListItemClickListener {

    public enum IntentExtra {
        TERM_ID
    }

    private SQLiteDatabase mDB;

    private View mLayout;

    private TextView mTitle;

    private TextView mDateRange;

    private TextView mStatus;

    private RecyclerView mCourseList;

    private CourseAdapter mCourseAdapter;

    private DegreeService degreeService;

    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        degreeService = new DegreeService(this);
        setContentView(R.layout.activity_detail_term);

        mLayout = findViewById(R.id.detailTermLayout);
        mTitle = findViewById(R.id.tv_detailterm_title);
        mDateRange = findViewById(R.id.tv_detailterm_daterange);
        mStatus = findViewById(R.id.tv_detailterm_status);
        mCourseList = findViewById(R.id.rv_courses);

        DegreeDBHelper dbHelper = DegreeDBHelper.getInstance(this);
        mDB = dbHelper.getReadableDatabase();

        Intent intent = getIntent();
        termId = -1;
        if(intent.hasExtra(IntentExtra.TERM_ID.name())){
            termId = intent.getIntExtra(IntentExtra.TERM_ID.name(), 0);
            informTermDetail(degreeService.getTermById(termId));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCourseList.setLayoutManager(layoutManager);
        mCourseAdapter = new CourseAdapter(degreeService.getCoursesByTerm(termId), this, CourseAdapter.TypeEnum.COURSE_TERM);
        mCourseList.setAdapter(mCourseAdapter);
        mCourseList.setHasFixedSize(true);
    }

    private void informTermDetail(Cursor cursor){
        if(cursor.moveToFirst()){
            mTitle.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_TITLE)));
            Date startDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_START_DATE)));
            Date endDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_END_DATE)));

            String startDateStr = DegreeUtils.getStringFromDate(startDate);
            String endDateStr = DegreeUtils.getStringFromDate(endDate);

            mDateRange.setText(startDateStr + " - " + endDateStr);
            mStatus.setText(DegreeTrackerContract.TermEntry.getStatusByStartEndDate(startDate, endDate));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, AddTermActivity.class);
            intent.putExtra(AddTermActivity.IntentExtra.MODE_EDITION.name(), true);
            intent.putExtra(IntentExtra.TERM_ID.name(), termId);
            startActivity(intent);
        } else if(id == R.id.action_delete){
            deleteTerm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm(){
        if(mCourseAdapter.getItemCount() > 0){
            Snackbar.make(mLayout, "The Term has courses assigned and cannot be deleted", Snackbar.LENGTH_LONG).show();
        }
        else {
            DialogDeleteClickListener dialogClickListener = new DialogDeleteClickListener();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_term_message)
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    @Override
    public void onListItemClick(int courseId) {
        Intent intent = new Intent(this, DetailCourseActivity.class);
        intent.putExtra(DetailCourseActivity.IntentExtra.COURSE_ID.name(), courseId);
        startActivity(intent);
    }

    private void returnToListTermActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    class DialogDeleteClickListener implements DialogInterface.OnClickListener{

        private boolean courseDeleted;
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {
            if(choice == DialogInterface.BUTTON_POSITIVE){
                degreeService.deleteTerm(termId);
                returnToListTermActivity();
            }
        }

        public boolean isCourseDeleted() {
            return courseDeleted;
        }
    }


}
