package com.exercise.fmart43.degreetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.exercise.fmart43.degreetracker.MainActivity;
import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.adapters.CourseAdapter;
import com.exercise.fmart43.degreetracker.adapters.TermAdapter;
import com.exercise.fmart43.degreetracker.data.DegreeService;

public class ListCourseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CourseAdapter.ListItemClickListener {

    private RecyclerView mCourseList;

    private CourseAdapter mCourseAdapter;

    private DegreeService degreeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        degreeService = new DegreeService(this);

        setContentView(R.layout.activity_list_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListCourseActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mCourseList = findViewById(R.id.rv_courses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCourseList.setLayoutManager(layoutManager);
        mCourseAdapter = new CourseAdapter(degreeService.getCourseList(), this, CourseAdapter.TypeEnum.COURSE_LIST);
        mCourseList.setAdapter(mCourseAdapter);
        mCourseList.setHasFixedSize(true);

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
        getMenuInflater().inflate(R.menu.list_course, menu);
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
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onListItemClick(int courseId) {
        Intent intent = new Intent(this, DetailCourseActivity.class);
        intent.putExtra(DetailCourseActivity.IntentExtra.COURSE_ID.name(), courseId);
        startActivity(intent);
    }
}
