package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name_header;
    TextView email_header;
    TextView medal;
    ImageView medalpic;
    TextView informationTechnology;
    TextView artandDesign;
    TextView business;
    TextView physics;
    TextView music;
    TextView english;
    TextView informationTechnologyComp;
    TextView ArtandDesignComp;
    TextView businessComp;
    TextView physicsComp;
    TextView musicComp;
    TextView englishComp;

    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView uname = (TextView)findViewById(R.id.tv_username);
        TextView email = (TextView)findViewById(R.id.tv_user_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name_header = (TextView) headerView.findViewById(R.id.name_header_drw);
        email_header = (TextView) headerView.findViewById(R.id.email_header_drw);

        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
        int id = activeIdPassing.getActiveId();
        databaseAccess.open();
        Cursor data = databaseAccess.getStudentData(Integer.toString(id));
        try {
            if(data.moveToFirst()){
                name_header.setText(data.getString(0));
                email_header.setText(data.getString(1));
                uname.setText(data.getString(0));
                email.setText(data.getString(1));
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

        //Courses Taken
        informationTechnology = findViewById(R.id.tv_informationTech);
        artandDesign = findViewById(R.id.tv_artandDesign);
        business = findViewById(R.id.tv_business);
        physics = findViewById(R.id.tv_physics);
        music = findViewById(R.id.tv_music);
        english = findViewById(R.id.tv_english);

        databaseAccess.open();
        String intech = Integer.toString(databaseAccess.getPurchasedCourse(id, 1)) + " Course Taken";
        String aad = Integer.toString(databaseAccess.getPurchasedCourse(id, 2)) + " Course Taken";
        String busi = Integer.toString(databaseAccess.getPurchasedCourse(id, 3))+ " Course Taken";
        String phy = Integer.toString(databaseAccess.getPurchasedCourse(id, 4))+ " Course Taken";
        String musi = Integer.toString(databaseAccess.getPurchasedCourse(id, 5))+ " Course Taken";
        String englis = Integer.toString(databaseAccess.getPurchasedCourse(id, 6))+ " Course Taken";

        informationTechnology.setText(intech);
        artandDesign.setText(aad);
        business.setText(busi);
        physics.setText(phy);
        music.setText(musi);
        english.setText(englis);

        //Course Completed
        informationTechnologyComp = findViewById(R.id.tv_informationTechComp);
        ArtandDesignComp = findViewById(R.id.tv_artandDesignComp);;
        businessComp = findViewById(R.id.tv_businessComp);
        physicsComp = findViewById(R.id.tv_physicsComp);;
        musicComp = findViewById(R.id.tv_musicComp);
        englishComp = findViewById(R.id.tv_englishComp);

        int ctr = 0;

        String Compintech = Integer.toString(databaseAccess.getCompletedCourse(id, 1,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 1,1);
        String Compaad = Integer.toString(databaseAccess.getCompletedCourse(id, 2,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 2,1);
        String Compbusi = Integer.toString(databaseAccess.getCompletedCourse(id, 3,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 3,1);
        String Compphy = Integer.toString(databaseAccess.getCompletedCourse(id, 4,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 4,1);
        String Compmusi = Integer.toString(databaseAccess.getCompletedCourse(id, 5,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 5,1);
        String Compenglis = Integer.toString(databaseAccess.getCompletedCourse(id, 6,1)) + " Course Completed";
        ctr += databaseAccess.getCompletedCourse(id, 6,1);

        informationTechnologyComp.setText(Compintech);
        ArtandDesignComp.setText(Compaad);
        businessComp.setText(Compbusi);
        physicsComp.setText(Compphy);
        musicComp.setText(Compmusi);
        englishComp.setText(Compenglis);
        databaseAccess.close();

        //Medal
        medal = findViewById(R.id.prof_medal);
        medalpic = findViewById(R.id.prof_pic);
        String award = " ";
        if(ctr <= 10){
            award = "Bronze Medal";
            activeIdPassing.setReward(award);
            medal.setText(award);
            medalpic.setBackgroundColor(getResources().getColor(R.color.colorBronze));
        }else if( ctr <= 20){
            award = "Silver Medal";
            activeIdPassing.setReward(award);
            medal.setText(award);
            medalpic.setBackgroundColor(getResources().getColor(R.color.colorSilver));
        }else{
            award = "Gold Medal";
            activeIdPassing.setReward(award);
            medal.setText(award);
            medalpic.setBackgroundColor(getResources().getColor(R.color.colorGold));
        }
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
            homepageIntent.putExtra("userID", id);
            startActivity(homepageIntent);
            finish();
        } else if (id == R.id.mycourse_drw) {
            Intent myCourseIntent = new Intent(this, MyCourses.class);
            myCourseIntent.putExtra("userID", id);
            startActivity(myCourseIntent);
            finish();
        } else if (id == R.id.profile_drw) {

        } else if (id == R.id.accsett_drw) {
            Intent accsettIntent = new Intent(this, AccountSetting.class);
            accsettIntent.putExtra("userID", id);
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
}
