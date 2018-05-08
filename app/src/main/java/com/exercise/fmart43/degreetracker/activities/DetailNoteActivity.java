package com.exercise.fmart43.degreetracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
        COURSE_NAME,
        NOTE_ID
    }

    private static final String TITLE_NOTE = "Sharing the note of course ";

    private CoordinatorLayout mLayout;

    private EditText mTextNote;

    private Button mCancel;

    private Button mSave;

    private FloatingActionButton floatingActionButton;

    private int courseId;

    private String courseName;

    private int noteId;

    private DegreeService degreeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.degreeService = new DegreeService(this);

        mLayout = new CoordinatorLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout layoutButtons = new LinearLayout(this);
        layoutButtons.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        mTextNote = new EditText(this);
        CoordinatorLayout.LayoutParams floatingActionButtonParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatingActionButtonParams.gravity = Gravity.RIGHT|Gravity.BOTTOM;
        Resources r = this.getResources();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        floatingActionButtonParams.setMargins(0, 0, margin, margin);
        floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setImageResource(R.drawable.ic_share_white_24dp);
        floatingActionButton.setLayoutParams(floatingActionButtonParams);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mimeType = "text/plain";
                String title = TITLE_NOTE + courseName;
                String text = TITLE_NOTE + courseName + "\n" + mTextNote.getText().toString();
                ShareCompat.IntentBuilder.from(DetailNoteActivity.this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(text)
                        .startChooser();
            }
        });

        mCancel = new Button(this);
        mCancel.setText("Back");
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
        ViewGroup.LayoutParams paramsTextNote = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextNote.setLayoutParams(paramsTextNote);
        mTextNote.setLines(10);
        linearLayout.addView(mTextNote);
        layoutButtons.addView(mCancel);
        layoutButtons.addView(mSave);
        linearLayout.addView(layoutButtons);
        mLayout.addView(linearLayout);
        mLayout.addView(floatingActionButton);
        setContentView(mLayout);

        Intent intent = getIntent();
        if(intent.hasExtra(IntentExtra.COURSE_ID.name())){
            courseId = intent.getIntExtra(IntentExtra.COURSE_ID.name(), 0);
        }
        if(intent.hasExtra(IntentExtra.COURSE_NAME.name())){
            courseName = intent.getStringExtra(IntentExtra.COURSE_NAME.name());
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
            Snackbar.make(mLayout, messageError, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(noteId > 0)
            getMenuInflater().inflate(R.menu.delete_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                if(choice == DialogInterface.BUTTON_POSITIVE) {
                    degreeService.deleteNote(noteId);
                    finish();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_note_message)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return super.onOptionsItemSelected(item);
    }

}
