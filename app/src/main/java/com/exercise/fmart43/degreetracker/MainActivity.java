package com.exercise.fmart43.degreetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.exercise.fmart43.degreetracker.activities.AddTermActivity;
import com.exercise.fmart43.degreetracker.activities.DetailTermActivity;
import com.exercise.fmart43.degreetracker.activities.ListCourseActivity;
import com.exercise.fmart43.degreetracker.activities.PreferencesShowActivity;
import com.exercise.fmart43.degreetracker.activities.SettingsActivity;
import com.exercise.fmart43.degreetracker.adapters.TermAdapter;
import com.exercise.fmart43.degreetracker.data.DegreeDBHelper;
import com.exercise.fmart43.degreetracker.data.DegreeService;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.NotificationTriggerUtilities;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TermAdapter.ListItemClickListener{

    private TermAdapter mTermAdapter;
    private RecyclerView mTermList;
    private DrawerLayout mLayout;

    private DegreeService degreeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        degreeService = new DegreeService(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        Recycler view Layout configuration
         */
        mLayout = findViewById(R.id.layout_main);
        mTermList = findViewById(R.id.rv_terms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTermList.setLayoutManager(layoutManager);
        mTermAdapter = new TermAdapter(degreeService.getTermList(), this);
        mTermList.setAdapter(mTermAdapter);
        mTermList.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTermActivity.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NotificationTriggerUtilities.degreeReminder(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;
        if (id == R.id.nav_terms) {
            intent = new Intent(this, MainActivity.class);
        } else if (id == R.id.nav_courses) {
            intent = new Intent(this, ListCourseActivity.class);
        } else if (id == R.id.nav_show_settings) {
            intent = new Intent(this, PreferencesShowActivity.class);
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onListItemClick(int termId) {
        Intent intent = new Intent(this, DetailTermActivity.class);
        intent.putExtra(DetailTermActivity.IntentExtra.TERM_ID.name(), termId);
        startActivity(intent);
    }

    @Override
    public void onListItemClickDelete(final int termId) {
        Cursor cursor = degreeService.getCoursesByTerm(termId);
        if(cursor.moveToFirst()){
            Snackbar.make(mLayout, "The Term has courses assigned and cannot be deleted", Snackbar.LENGTH_LONG).show();
            return;
        }
        else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {
                    switch (choice) {
                        case DialogInterface.BUTTON_POSITIVE:
                            degreeService.deleteTerm(termId);
                            mTermAdapter = new TermAdapter(degreeService.getTermList(), MainActivity.this);
                            mTermList.setAdapter(mTermAdapter);
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_term_message)
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
    }

    @Override
    public void onListItemClickEdit(int termId) {
        Intent intent = new Intent(this, AddTermActivity.class);
        intent.putExtra(AddTermActivity.IntentExtra.MODE_EDITION.name(), true);
        intent.putExtra(DetailTermActivity.IntentExtra.TERM_ID.name(), termId);
        startActivity(intent);
    }


}
