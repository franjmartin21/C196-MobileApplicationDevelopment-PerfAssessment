package com.exercise.fmart43.degreetracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class DegreeService {

    private static DegreeService sInstance;

    private DegreeDBHelper dbHelper;

    public DegreeService(Context context){
        dbHelper = DegreeDBHelper.getInstance(context);
    }

    public Cursor getTermList(){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DegreeTrackerContract.TermEntry._ID);
    }

    public Cursor getTermById(int termId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.TermEntry._ID + "=?",
                (new String[]{String.valueOf(termId)}),
                null,
                null,
                null);
    }

    public Cursor getTermsPriorDate(Date date){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                "date(" + DegreeTrackerContract.TermEntry.COLUMN_END_DATE + ")>date(?)",
                new String[]{DegreeTrackerContract.DB_DATE_FORMAT.format(date)},
                null,
                null,
                null);
    }

    public Cursor getCourseList(){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        String rawQuery = "" +
                "SELECT      c."+DegreeTrackerContract.CourseEntry._ID + "," +
                            "c."+DegreeTrackerContract.CourseEntry.COLUMN_TITLE + ", " +
                            "c."+DegreeTrackerContract.CourseEntry.COLUMN_START_DATE + ", " +
                            "c."+DegreeTrackerContract.CourseEntry.COLUMN_END_DATE + ", " +
                            "c."+DegreeTrackerContract.CourseEntry.COLUMN_STATUS + ", "+
                            "t."+DegreeTrackerContract.TermEntry.COLUMN_TITLE + " " + DegreeTrackerContract.CourseEntry.LABEL_TERM_TITLE +
                " FROM " + DegreeTrackerContract.CourseEntry.TABLE_NAME +" c" +
                " INNER JOIN " + DegreeTrackerContract.TermEntry.TABLE_NAME + " t" +
                " ON c." + DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID + "=t." + DegreeTrackerContract.TermEntry._ID +
                " ORDER BY c." + DegreeTrackerContract.CourseEntry._ID;

       return mDB.rawQuery(
                rawQuery,
                null
        );
    }

    public Cursor getCoursesByTerm(int termId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.CourseEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID + "=?",
                new String[]{String.valueOf(termId)},
                null,
                null,
                DegreeTrackerContract.CourseEntry._ID);
    }

    public Cursor getCourseById(int courseId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.CourseEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.CourseEntry._ID + "=?",
                new String[]{String.valueOf(courseId)},
                null,
                null,
                null);
    }

    public Cursor getAssessmentList(){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        String rawQuery = "" +
                "SELECT      a."+DegreeTrackerContract.AssessmentEntry._ID + "," +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE+ ", " +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_DATE+ ", " +
                "c."+DegreeTrackerContract.CourseEntry.COLUMN_TITLE + " " + DegreeTrackerContract.AssessmentEntry.LABEL_COURSE_TITLE +
                " FROM " + DegreeTrackerContract.AssessmentEntry.TABLE_NAME +" a" +
                " INNER JOIN " + DegreeTrackerContract.CourseEntry.TABLE_NAME + " c" +
                " ON a." + DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + "=c." + DegreeTrackerContract.CourseEntry._ID +
                " ORDER BY a." + DegreeTrackerContract.AssessmentEntry._ID;

        return mDB.rawQuery(
                rawQuery,
                null
        );
    }

    public Cursor getAssessmentsByCourseId(int courseId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.AssessmentEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + "=?",
                new String[]{String.valueOf(courseId)},
                null,
                null,
                null);
    }

    public Cursor getAssessmentById(int assessmentId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        String rawQuery = "" +
                "SELECT      a."+DegreeTrackerContract.AssessmentEntry._ID + "," +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE + ", " +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_DATE + ", " +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE +"," +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_TIMESTAMP +"," +
                "a."+DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID +"," +
                "c."+DegreeTrackerContract.CourseEntry.COLUMN_TITLE + " " + DegreeTrackerContract.AssessmentEntry.LABEL_COURSE_TITLE +
                " FROM " + DegreeTrackerContract.AssessmentEntry.TABLE_NAME +" a" +
                " INNER JOIN " + DegreeTrackerContract.CourseEntry.TABLE_NAME + " c" +
                " ON a." + DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + "=c." + DegreeTrackerContract.CourseEntry._ID +
                " WHERE a." + DegreeTrackerContract.AssessmentEntry._ID + "=?";

        return mDB.rawQuery(
                rawQuery,
                new String[]{String.valueOf(assessmentId)}
        );
    }

    public Cursor getNotesByCourseId(int courseId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.NoteEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.NoteEntry.COLUMN_COURSE_ID + "=?",
                new String[]{String.valueOf(courseId)},
                null,
                null,
                null);
    }

    public long insertTerm(String title, Date startDate, Date endDate){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, title);
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, DegreeTrackerContract.getStringForDatabase(startDate));
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, DegreeTrackerContract.getStringForDatabase(endDate));

        return mDB.insert(DegreeTrackerContract.TermEntry.TABLE_NAME, null, contentValues);
    }

    public long updateTerm(String title, Date startDate, Date endDate, int termId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, title);
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, DegreeTrackerContract.getStringForDatabase(startDate));
        contentValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, DegreeTrackerContract.getStringForDatabase(endDate));

        return mDB.update(DegreeTrackerContract.TermEntry.TABLE_NAME, contentValues, DegreeTrackerContract.TermEntry._ID + "=?",new String[]{String.valueOf(termId)} );
    }

    public int deleteTerm(int termId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        return mDB.delete(DegreeTrackerContract.TermEntry.TABLE_NAME, DegreeTrackerContract.TermEntry._ID + "=?",new String[]{String.valueOf(termId)});
    }

    public long insertCourse(String title, Date startDate, Date endDate, String status, String mentor, String phoneNumber, String email, int termId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_TITLE, title);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_START_DATE, DegreeTrackerContract.getStringForDatabase(startDate));
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE, DegreeTrackerContract.getStringForDatabase(endDate));
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_STATUS, status);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME, mentor);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER, phoneNumber);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS, email);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID, termId);

        return mDB.insert(DegreeTrackerContract.CourseEntry.TABLE_NAME, null, contentValues);
    }

    public long updateCourse(String title, Date startDate, Date endDate, String status, String mentor, String phoneNumber, String email, int termId, int courseId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_TITLE, title);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_START_DATE, DegreeTrackerContract.getStringForDatabase(startDate));
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE, DegreeTrackerContract.getStringForDatabase(endDate));
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_STATUS, status);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME, mentor);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER, phoneNumber);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS, email);
        contentValues.put(DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID, termId);

        return mDB.update(DegreeTrackerContract.CourseEntry.TABLE_NAME, contentValues, DegreeTrackerContract.CourseEntry._ID + "=?",new String[]{String.valueOf(courseId)} );
    }

    public int deleteCourse(int courseId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        return mDB.delete(DegreeTrackerContract.CourseEntry.TABLE_NAME, DegreeTrackerContract.CourseEntry._ID + "=?",new String[]{String.valueOf(courseId)});
    }

    public long insertAssessment(String title, Date date, String type, int courseId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE, title );
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_DATE, DegreeTrackerContract.getStringForDatabase(date));
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE, type);
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID, courseId);

        return mDB.insert(DegreeTrackerContract.AssessmentEntry.TABLE_NAME, null, contentValues);
    }

    public long updateAssessment(String title, Date date, String type, int courseId, int assessmentId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE, title );
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_DATE, DegreeTrackerContract.getStringForDatabase(date));
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE, type);
        contentValues.put(DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID, courseId );

        return mDB.update(DegreeTrackerContract.AssessmentEntry.TABLE_NAME, contentValues, DegreeTrackerContract.AssessmentEntry._ID + "=?",new String[]{String.valueOf(assessmentId)} );
    }

    public int deleteAssessment(int courseId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        return mDB.delete(DegreeTrackerContract.AssessmentEntry.TABLE_NAME, DegreeTrackerContract.AssessmentEntry._ID + "=?",new String[]{String.valueOf(courseId)});
    }

    public Cursor getNoteById(int noteId){
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();

        return mDB.query(DegreeTrackerContract.NoteEntry.TABLE_NAME,
                null,
                DegreeTrackerContract.NoteEntry._ID + "=?",
                (new String[]{String.valueOf(noteId)}),
                null,
                null,
                null);
    }



    public long insertNote(String text, int courseId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.NoteEntry.COLUMN_TEXT, text);
        contentValues.put(DegreeTrackerContract.NoteEntry.COLUMN_COURSE_ID, courseId);

        return mDB.insert(DegreeTrackerContract.NoteEntry.TABLE_NAME, null, contentValues);
    }

    public long updateNote(String text, int courseId, int noteId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DegreeTrackerContract.NoteEntry.COLUMN_TEXT, text);
        contentValues.put(DegreeTrackerContract.NoteEntry.COLUMN_COURSE_ID, courseId);

        return mDB.update(DegreeTrackerContract.NoteEntry.TABLE_NAME, contentValues, DegreeTrackerContract.NoteEntry._ID + "=?",new String[]{String.valueOf(noteId)} );
    }

    public int deleteNote(int noteId){
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        return mDB.delete(DegreeTrackerContract.AssessmentEntry.TABLE_NAME, DegreeTrackerContract.NoteEntry._ID + "=?",new String[]{String.valueOf(noteId)});
    }
}
