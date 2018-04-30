package com.exercise.fmart43.degreetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.exercise.fmart43.degreetracker.data.DegreeDBHelper;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    /* Context used to perform operations on the database and create WaitlistDbHelper */
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    /* Class reference to help load the constructor on runtime */
    private final Class mDbHelperClass = DegreeDBHelper.class;

    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    @Test
    public void create_database_test() throws Exception{


        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();


        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        DegreeTrackerContract.TermEntry.TABLE_NAME + "'",
                null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase = "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase, tableNameCursor.moveToFirst());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertEquals("Error: Your database was created without the expected tables.",
                DegreeTrackerContract.TermEntry.TABLE_NAME, tableNameCursor.getString(0));

        /* Always close a cursor when you are done with it */
        tableNameCursor.close();
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * The purpose is to test that the database is working as expected
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void insert_single_record_test() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        String VALUE_TITLE = "Term 1";
        String VALUE_START_DATE = "2018-01-01";
        String VALUE_END_DATE = "2018-02-01";
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());
        assertEquals(VALUE_TITLE, wCursor.getString(wCursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_TITLE)));
        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }


    @Test
    public void queryByDate() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        String VALUE_TITLE = "Term 1";
        String VALUE_START_DATE = "2018-01-01";
        String VALUE_END_DATE = "2018-02-01";
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                "date(" + DegreeTrackerContract.TermEntry.COLUMN_END_DATE + ")<date(?)",
                new String[]{DegreeTrackerContract.DB_DATE_FORMAT.format(new Date())},
                null,
                null,
                null);
        boolean found = wCursor.moveToFirst();

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());
        wCursor.close();
        dbHelper.close();
    }


    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void autoincrement_test() throws Exception{

        /* First, let's ensure we have some values in our table initially */
        insert_single_record_test();

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        String VALUE_TITLE = "Term 1";
        String VALUE_START_DATE = "2018-01-01";
        String VALUE_END_DATE = "2018-02-01";
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE);


        ContentValues testValues2 = new ContentValues();
        String VALUE_TITLE2 = "Term 2";
        String VALUE_START_DATE2 = "2018-03-01";
        String VALUE_END_DATE2 = "2018-04-01";
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE2);
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE2);
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE2);
        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues2);

        assertEquals("ID Autoincrement test failed!",firstRowId + 1, secondRowId);

    }


    /**
     * Tests that onUpgrade works by inserting 2 rows then calling onUpgrade and verifies that the
     * database has been successfully dropped and recreated by checking that the database is there
     * but empty
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void upgrade_database_test() throws Exception{

        /* Insert 2 rows before we upgrade to check that we dropped the database correctly */

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        String VALUE_TITLE = "Term 1";
        String VALUE_START_DATE = "2018-01-01";
        String VALUE_END_DATE = "2018-02-01";
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE);
        testValues.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE);

        ContentValues testValues2 = new ContentValues();
        String VALUE_TITLE2 = "Term 2";
        String VALUE_START_DATE2 = "2018-03-01";
        String VALUE_END_DATE2 = "2018-04-01";
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_TITLE, VALUE_TITLE2);
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_START_DATE, VALUE_START_DATE2);
        testValues2.put(DegreeTrackerContract.TermEntry.COLUMN_END_DATE, VALUE_END_DATE2);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                null,
                testValues2);


        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        DegreeTrackerContract.TermEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                DegreeTrackerContract.TermEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */

        assertEquals("Database doesn't seem to have been dropped successfully when upgrading",
                DegreeTrackerContract.TermEntry.NUM_INITIAL_TERMS,
                wCursor.getCount());

        tableNameCursor.close();
        database.close();
    }


    /**
     * Deletes the entire database.
     */
    void deleteTheDatabase(){
        try {
            /* Use reflection to get the database name from the db helper class */
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the DegreeDBHelper");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }

}
