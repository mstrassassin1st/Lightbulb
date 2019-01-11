package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.itdivision.lightbulb.Adapter.HomepageRecyclerViewAdapter;
import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Dialogs.DialogPreview;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;
import app.itdivision.lightbulb.Model.Course;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    List<Course> courseList;
    RecyclerView homepageRecycler;
    Spinner hmSelectCategory;
    TextView name_header;
    TextView email_header;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(Homepage.this);
    DialogPreview dialogPreview = new DialogPreview();


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("Select Category..")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try{
                Cursor cursor = databaseAccess.getCourses();
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            } catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }

        }else if(text.equals("Information Technology")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(1);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }

        }else if(text.equals("Art and Design")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(2);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }
        }else if(text.equals("Physics")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(4);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }
        }else if(text.equals("Music")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(5);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }

        }else if(text.equals("English")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(6);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }

        }else if(text.equals("Business")){
            courseList = new ArrayList<>();
            databaseAccess.open();
            try {
                Cursor cursor = databaseAccess.getCustomCourses(3);
                while(cursor.moveToNext()){
                    byte[] imgByte = cursor.getBlob(3);
                    Bitmap cover = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    courseList.add(new Course(cursor.getString(0),cursor.getString(1), cursor.getInt(2), cover));
                }
                cursor.close();
                databaseAccess.close();
            }catch (Exception e){
                Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }

        }else{
            courseList = new ArrayList<>();

        }

        homepageRecycler = (RecyclerView) findViewById(R.id.recyclerHomepage);
        HomepageRecyclerViewAdapter homepageAdapter = new HomepageRecyclerViewAdapter(this, courseList);
        homepageRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        homepageRecycler.setAdapter(homepageAdapter);
        homepageAdapter.setOnBtnPrevCourseClickListener(new HomepageRecyclerViewAdapter.OnBtnPrevCourseClickListener() {
            @Override
            public void onBtnPrevCourseClicked(String CourseName) {
                runDialogPrev(CourseName);
            }
        });
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navdrawer
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name_header = (TextView) headerView.findViewById(R.id.name_header_drw);
        email_header = (TextView) headerView.findViewById(R.id.email_header_drw);

        Intent intent = getIntent();
        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
        int id = activeIdPassing.getActiveId();
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //Category Selection
        hmSelectCategory = (Spinner) findViewById(R.id.select_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.homepage_selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hmSelectCategory.setAdapter(adapter);
        hmSelectCategory.setOnItemSelectedListener(this);

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
            //
        } else if (id == R.id.mycourse_drw) {
            Intent myCourseIntent = new Intent(Homepage.this, MyCourses.class);
            myCourseIntent.putExtra("userID", id);
            startActivity(myCourseIntent);
            finish();
        } else if (id == R.id.profile_drw) {
            Intent profileIntent = new Intent(Homepage.this, Profile.class);
            profileIntent.putExtra("userID", id);
            startActivity(profileIntent);
            finish();
        } else if (id == R.id.accsett_drw) {
            Intent accsettIntent = new Intent(Homepage.this, AccountSetting.class);
            accsettIntent.putExtra("userID", id);
            startActivity(accsettIntent);
            finish();
        }  else if (id == R.id.aboutus_drw) {
            //
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void runDialogPrev(String CourseName){
        DialogPreview dialogPreview = new DialogPreview();
        dialogPreview.setParams(CourseName);
        dialogPreview.show(getSupportFragmentManager(), "Preview");
    }


}
