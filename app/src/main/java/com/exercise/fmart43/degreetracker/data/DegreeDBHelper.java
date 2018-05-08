package com.exercise.fmart43.degreetracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exercise.fmart43.degreetracker.util.DegreeUtils;

public class DegreeDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "degree.db";

    private static final int DATABASE_VERSION = 1;

    private static DegreeDBHelper sInstance;

    private DegreeDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       createTermTable(sqLiteDatabase);

       createCourseTable(sqLiteDatabase);

       createAssessmentTable(sqLiteDatabase);

       createNoteTable(sqLiteDatabase);

    }

    private void createTermTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TERM_TABLE = "CREATE TABLE " +
                DegreeTrackerContract.TermEntry.TABLE_NAME + " (" +
                DegreeTrackerContract.TermEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DegreeTrackerContract.TermEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DegreeTrackerContract.TermEntry.COLUMN_START_DATE + " DATE NOT NULL, " +
                DegreeTrackerContract.TermEntry.COLUMN_END_DATE + " DATE NOT NULL, " +
                DegreeTrackerContract.TermEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TERM_TABLE);
        final String SQL_INSERT_TERM_1 = "INSERT INTO " + DegreeTrackerContract.TermEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.TermEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.TermEntry.COLUMN_START_DATE + "," +
                DegreeTrackerContract.TermEntry.COLUMN_END_DATE + ")" +
                " VALUES('Term 1', '2018-01-01', '2018-06-30')";

        final String SQL_INSERT_TERM_2 = "INSERT INTO " + DegreeTrackerContract.TermEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.TermEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.TermEntry.COLUMN_START_DATE + "," +
                DegreeTrackerContract.TermEntry.COLUMN_END_DATE + ")" +
                " VALUES('Term 2', '2018-07-01', '2018-12-31')";

        sqLiteDatabase.execSQL(SQL_INSERT_TERM_1);
        sqLiteDatabase.execSQL(SQL_INSERT_TERM_2);
    }

    private void createCourseTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_COURSE_TABLE = "CREATE TABLE " +
                DegreeTrackerContract.CourseEntry.TABLE_NAME + " (" +
                DegreeTrackerContract.CourseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DegreeTrackerContract.CourseEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DegreeTrackerContract.CourseEntry.COLUMN_START_DATE + " DATE, " +
                DegreeTrackerContract.CourseEntry.COLUMN_END_DATE + " DATE, " +
                DegreeTrackerContract.CourseEntry.COLUMN_STATUS + " TEXT, " +
                DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME + " TEXT, " +
                DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER + " TEXT, " +
                DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS + " TEXT, " +
                DegreeTrackerContract.CourseEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID + " INTEGER" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_COURSE_TABLE);
        final String SQL_INSERT_COURSE_1 = "INSERT INTO " + DegreeTrackerContract.CourseEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.CourseEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_START_DATE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_END_DATE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_STATUS + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID  + ")" +
                " VALUES('Mobile Application Development', '2018-01-01', '2018-01-31', 'IN_PROGRESS', 'Julius Juliany', '1234123412', 'julius@julianny.com', 1)";
        sqLiteDatabase.execSQL(SQL_INSERT_COURSE_1);

        final String SQL_INSERT_COURSE_2 = "INSERT INTO " + DegreeTrackerContract.CourseEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.CourseEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_START_DATE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_END_DATE + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_STATUS + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_MENTOR_NAME + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_PHONE_NUMBER + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_EMAIL_ADDRESS + "," +
                DegreeTrackerContract.CourseEntry.COLUMN_TERM_ID  + ")" +
                " VALUES('Algorithms', '2018-02-01', '2018-02-28', 'PENDING', 'Julius Juliany', '4353454354', 'julius@julianny.com', 1)";
        sqLiteDatabase.execSQL(SQL_INSERT_COURSE_2);
    }

    private void createAssessmentTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ASSESSMENT_TABLE = "CREATE TABLE " +
                DegreeTrackerContract.AssessmentEntry.TABLE_NAME + " (" +
                DegreeTrackerContract.AssessmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                DegreeTrackerContract.AssessmentEntry.COLUMN_DATE + " DATE, " +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + " INTEGER" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_ASSESSMENT_TABLE);

        final String SQL_INSERT_ASSESSMENT_1 = "INSERT INTO " + DegreeTrackerContract.AssessmentEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_DATE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + ")" +
                " VALUES('Final Assessment', '" + DegreeTrackerContract.AssessmentEntry.TypeAssessment.OBJECTIVE + "','2018-01-31', 1)";

        sqLiteDatabase.execSQL(SQL_INSERT_ASSESSMENT_1);

        final String SQL_INSERT_ASSESSMENT_2 = "INSERT INTO " + DegreeTrackerContract.AssessmentEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_DATE + "," +
                DegreeTrackerContract.AssessmentEntry.COLUMN_COURSE_ID + ")" +
                " VALUES('Perf Assessment', '" + DegreeTrackerContract.AssessmentEntry.TypeAssessment.PERFORMANCE + "','2018-02-28', 1)";
        sqLiteDatabase.execSQL(SQL_INSERT_ASSESSMENT_2);
    }

    private void createNoteTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " +
                DegreeTrackerContract.NoteEntry.TABLE_NAME + " (" +
                DegreeTrackerContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DegreeTrackerContract.NoteEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                DegreeTrackerContract.NoteEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                DegreeTrackerContract.NoteEntry.COLUMN_COURSE_ID + " INTEGER " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);


        final String SQL_INSERT_NOTE_1 = "INSERT INTO " + DegreeTrackerContract.NoteEntry.TABLE_NAME +"(" +
                DegreeTrackerContract.NoteEntry.COLUMN_TEXT + "," +
                DegreeTrackerContract.NoteEntry.COLUMN_COURSE_ID + ")" +
                " VALUES('Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',"+
                "1)";
        sqLiteDatabase.execSQL(SQL_INSERT_NOTE_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DegreeTrackerContract.TermEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static synchronized DegreeDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DegreeDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
}
