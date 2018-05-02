package com.exercise.fmart43.degreetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.adapters.AssessmentAdapter;
import com.exercise.fmart43.degreetracker.adapters.CourseAdapter;
import com.exercise.fmart43.degreetracker.adapters.NoteAdapter;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;

import static com.exercise.fmart43.degreetracker.R.id.forever;
import static com.exercise.fmart43.degreetracker.R.id.menu_fab;

public class DetailCourseActivity extends AppCompatActivity implements AssessmentAdapter.ListItemClickListener, NoteAdapter.ListItemClickListener {

    public enum IntentExtra {
        COURSE_ID
    }

    public enum RequestCode{
        ASSESSMENT_ACTIVITY(1000),
        NOTE_ACTIVITY(1001);

        int code;
        RequestCode(int code){
            this.code = code;
        }

    }

    private static final String EMPTY_LABEL = "<<empty>>";

    private DegreeService degreeService;

    private TextView mTitle;
    private TextView mStatus;
    private TextView mDateRange;
    private TextView mMentor;
    private TextView mPhone;
    private TextView mEmail;

    private RecyclerView mAssessmentList;
    private RecyclerView mNoteList;

    private AssessmentAdapter mAssessmentAdapter;
    private NoteAdapter mNoteAdapter;

    private int courseIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        degreeService = new DegreeService(this);

        setContentView(R.layout.activity_detail_course);
        mTitle = findViewById(R.id.tv_coursedetail_title);
        mStatus = findViewById(R.id.tv_coursedetail_status);
        mDateRange = findViewById(R.id.tv_coursedetail_daterange);
        mMentor = findViewById(R.id.tv_coursedetail_mentor);
        mPhone = findViewById(R.id.tv_coursedetail_phone);
        mEmail = findViewById(R.id.tv_coursedetail_email);

        mAssessmentList = findViewById(R.id.rv_assessment);
        mNoteList = findViewById(R.id.rv_notes);

        Intent intent = getIntent();

        if(intent.hasExtra(IntentExtra.COURSE_ID.name())){
            courseIdSelected = intent.getIntExtra(IntentExtra.COURSE_ID.name(), 0);
            informCourseDetail(degreeService.getCourseById(courseIdSelected));
        }
        final com.getbase.floatingactionbutton.FloatingActionsMenu floatingActionsMenu = findViewById(menu_fab);


        com.getbase.floatingactionbutton.FloatingActionButton fabAssessmentNew = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.assessment_new);
        fabAssessmentNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(DetailCourseActivity.this, DetailAssessmentActivity.class);
            intent.putExtra(DetailAssessmentActivity.IntentExtra.COURSE_ID.name(), courseIdSelected);
            startActivityForResult(intent, RequestCode.ASSESSMENT_ACTIVITY.code);
            floatingActionsMenu.collapse();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fabNoteNew = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.note_new);
        fabNoteNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailCourseActivity.this, DetailNoteActivity.class);
                intent.putExtra(DetailNoteActivity.IntentExtra.COURSE_ID.name(), courseIdSelected);
                startActivityForResult(intent, RequestCode.NOTE_ACTIVITY.code);
                floatingActionsMenu.collapse();
            }
        });

        informAssessments(degreeService.getAssessmentsByCourseId(courseIdSelected));
        informNotes(degreeService.getNotesByCourseId(courseIdSelected));
    }

    private void informCourseDetail(Cursor cursor){
        if(cursor.moveToFirst()){
            mTitle.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_TITLE)));
            Date startDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_START_DATE)));
            Date endDate = DegreeTrackerContract.getDateFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE)));

            String startDateStr = DegreeUtils.getStringFromDate(startDate);
            String endDateStr = DegreeUtils.getStringFromDate(endDate);

            mDateRange.setText(startDateStr + " - " + endDateStr);
            mStatus.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_STATUS)));
            String mentor = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS));

            mMentor.setText(mentor != null ? mentor: EMPTY_LABEL);
            mPhone.setText(phone != null ? phone: EMPTY_LABEL);
            mEmail.setText(email != null? email: EMPTY_LABEL);
        }
    }

    private void informAssessments(Cursor cursor){
        LinearLayoutManager layoutManagerAssessment = new LinearLayoutManager(this);
        mAssessmentList.setLayoutManager(layoutManagerAssessment);
        mAssessmentAdapter = new AssessmentAdapter(cursor, this);
        mAssessmentList.setAdapter(mAssessmentAdapter);
        mAssessmentList.setHasFixedSize(true);
    }

    private void informNotes(Cursor cursor){
        LinearLayoutManager layoutManagerNotes = new LinearLayoutManager(this);
        mNoteList.setLayoutManager(layoutManagerNotes);
        mNoteAdapter = new NoteAdapter(cursor, this);
        mNoteList.setAdapter(mNoteAdapter);
        mNoteList.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, AddCourseActivity.class);
            intent.putExtra(AddCourseActivity.IntentExtra.MODE_EDITION.name(), true);
            intent.putExtra(AddCourseActivity.IntentExtra.COURSE_ID.name(), courseIdSelected);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int assessmentId) {
        Intent intent = new Intent(this, DetailAssessmentActivity.class);
        intent.putExtra(DetailAssessmentActivity.IntentExtra.COURSE_ID.name(), courseIdSelected);
        intent.putExtra(DetailAssessmentActivity.IntentExtra.ASSESSMENT_ID.name(), assessmentId);
        startActivityForResult(intent, RequestCode.ASSESSMENT_ACTIVITY.code);
    }

    @Override
    public void onNoteItemClick(int noteId) {
        Intent intent = new Intent(this, DetailNoteActivity.class);
        intent.putExtra(DetailNoteActivity.IntentExtra.COURSE_ID.name(), courseIdSelected);
        intent.putExtra(DetailNoteActivity.IntentExtra.NOTE_ID.name(), noteId);
        startActivityForResult(intent, RequestCode.NOTE_ACTIVITY.code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        informCourseDetail(degreeService.getCourseById(courseIdSelected));
        informAssessments(degreeService.getAssessmentsByCourseId(courseIdSelected));
        informNotes(degreeService.getNotesByCourseId(courseIdSelected));
    }
}
