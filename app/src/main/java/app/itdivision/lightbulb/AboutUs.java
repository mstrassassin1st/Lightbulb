package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.TextView;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class AboutUs extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name_header;
    TextView email_header;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        name_header = headerView.findViewById(R.id.name_header_drw);
        email_header = headerView.findViewById(R.id.email_header_drw);

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
            Intent homeIntent = new Intent(this, Homepage.class);
            startActivity(homeIntent);
            finish();
        } else if (id == R.id.mycourse_drw) {
            Intent mycourseIntent = new Intent(this, MyCourses.class);
            startActivity(mycourseIntent);
            finish();
        } else if (id == R.id.profile_drw) {
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
            finish();
        } else if (id == R.id.accsett_drw) {
            Intent accsettIntent = new Intent(this, AccountSetting.class);
            startActivity(accsettIntent);
            finish();
        } else if (id == R.id.aboutus_drw) {
            //
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
