package com.exercise.fmart43.degreetracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.text.ParseException;
import java.util.Date;

public class DetailNoteActivity extends AppCompatActivity {

    public enum IntentExtra {
        COURSE_ID,
        NOTE_ID
    }

    private LinearLayout mlayout;

    private TextView mDateTimeNote;

    private EditText mTextNote;

    private Button mCancel;

    private Button mSave;

    private int courseId;

    private int noteId;

    private DegreeService degreeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.degreeService = new DegreeService(this);


        mlayout = new LinearLayout(this);
        LinearLayout layoutButtons = new LinearLayout(this);
        layoutButtons.setOrientation(LinearLayout.HORIZONTAL);
        mlayout.setOrientation(LinearLayout.VERTICAL);
        mDateTimeNote = new TextView(this);
        mTextNote = new EditText(this);
        mCancel = new Button(this);
        mCancel.setText("Cancel");
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSave = new Button(this);
        mSave.setText("Save");
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                finish();
            }
        });
        ViewGroup.LayoutParams paramsExample = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextNote.setLayoutParams(paramsExample);
        mTextNote.setLines(10);
        mDateTimeNote.setText("01/01/2018 08:90");
        mlayout.addView(mDateTimeNote);
        mlayout.addView(mTextNote);
        layoutButtons.addView(mCancel);
        layoutButtons.addView(mSave);
        mlayout.addView(layoutButtons);
        setContentView(mlayout);

        Intent intent = getIntent();
        if(intent.hasExtra(IntentExtra.COURSE_ID.name())){
            courseId = intent.getIntExtra(IntentExtra.COURSE_ID.name(), 0);
        }
        if(intent.hasExtra(IntentExtra.NOTE_ID.name())){
            noteId = intent.getIntExtra(IntentExtra.NOTE_ID.name(), 0);
            informNoteData();
        }
    }

    private void informNoteData(){
        Cursor cursor = degreeService.getNoteById(noteId);
        if(cursor.moveToFirst()) {
            Date date = DegreeTrackerContract.getDateTimeFromDBStrValue(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.NoteEntry.COLUMN_TIMESTAMP)));
            mDateTimeNote.setText(DegreeUtils.getStringFromDateTime(date));
            mTextNote.setText(cursor.getString(cursor.getColumnIndex(DegreeTrackerContract.NoteEntry.COLUMN_TEXT)));
        }
    }

    private String validate(){
        StringBuilder messageError = new StringBuilder();
        String textNote = mTextNote.getText().toString();
        if(textNote == null || textNote.isEmpty()) messageError.append(getString(R.string.addTerm_title_notinformed)).append("\n");

        return messageError.toString();
    }

    private void saveNote(){
        String messageError = validate();
        if(messageError == null || messageError.isEmpty()) {
            if (noteId > 0) {
                degreeService.updateNote(mTextNote.getText().toString(), courseId, noteId);
            } else {
                degreeService.insertNote(mTextNote.getText().toString(), courseId);
            }
        } else {
            Snackbar.make(mlayout, messageError, Snackbar.LENGTH_LONG).show();
        }
    }
}
