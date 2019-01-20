package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.itdivision.lightbulb.Adapter.MyCoursesRecyclerViewAdapter;
import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;
import app.itdivision.lightbulb.Model.Course;
import app.itdivision.lightbulb.Model.MyCourse;

public class MyCourses extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    List<MyCourse> myCourseList;
    RecyclerView myCourseRecycler;
    Spinner statusSpinner;
    TextView name_header;
    TextView email_header;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MyCourses.this);
    ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idPass = activeIdPassing.getActiveId();
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("Select Completion Status..")){
            myCourseList = new ArrayList<>();
            databaseAccess.open();
            try{
                Cursor cursor = databaseAccess.getMyCourses(idPass);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(4);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    myCourseList.add(new MyCourse(cursor.getString(0), cursor.getString(1), cursor.getInt(3) , cover, cursor.getInt(2)));
                }
                cursor.close();
            }catch (Exception e){
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            databaseAccess.close();
        }else if(text.equals("ON GOING")){
            myCourseList = new ArrayList<>();
            databaseAccess.open();
            Cursor cursor = databaseAccess.getMyCoursesDetail(idPass, 0);
            try{
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(4);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    myCourseList.add(new MyCourse(cursor.getString(0), cursor.getString(1), cursor.getInt(3) , cover, cursor.getInt(2)));
                }

            }catch (Exception e){
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            cursor.close();
            databaseAccess.close();
        }else if(text.equals("COMPLETED")){
            myCourseList = new ArrayList<>();
            databaseAccess.open();
            Cursor cursor = databaseAccess.getMyCoursesDetail(idPass, 1);
            try{
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(4);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    myCourseList.add(new MyCourse(cursor.getString(0), cursor.getString(1), cursor.getInt(3) , cover, cursor.getInt(2)));
                }
            }catch (Exception e){
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            cursor.close();
            databaseAccess.close();
        }else{

        }

        myCourseRecycler = (RecyclerView) findViewById(R.id.recyclerMyCourses);
        MyCoursesRecyclerViewAdapter myCoursesAdapter = new MyCoursesRecyclerViewAdapter(this, myCourseList);
        myCourseRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        myCourseRecycler.setAdapter(myCoursesAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        int id = activeIdPassing.getActiveId();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name_header = (TextView) headerView.findViewById(R.id.name_header_drw);
        email_header = (TextView) headerView.findViewById(R.id.email_header_drw);

        databaseAccess.open();
        Cursor data = databaseAccess.getStudentData(Integer.toString(id));
        try {
            if(data.moveToFirst()){
                name_header.setText(data.getString(0));
                email_header.setText(data.getString(1));
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        data.close();
        databaseAccess.close();

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navdrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //Completed Status Selection
        statusSpinner = (Spinner) findViewById(R.id.select_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mycourses_selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homepage_drw) {
            Intent homepageIntent = new Intent(this, Homepage.class);
            startActivity(homepageIntent);
            finish();
        } else if (id == R.id.mycourse_drw) {

        } else if (id == R.id.profile_drw) {
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
            finish();
        } else if (id == R.id.accsett_drw) {
            Intent accsettIntent = new Intent(this, AccountSetting.class);
            startActivity(accsettIntent);
            finish();
        } else if (id == R.id.aboutus_drw) {
            Intent aboutusIntent = new Intent(this, AboutUs.class);
            startActivity(aboutusIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
