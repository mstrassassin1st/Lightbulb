package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Dialogs.DialogEmail;
import app.itdivision.lightbulb.Dialogs.DialogPassword;
import app.itdivision.lightbulb.Dialogs.DialogUsername;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class AccountSetting extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogUsername.DialogUsernameListener, DialogEmail.DialogEmailListener {

    CardView card_username;
    CardView card_email;
    CardView card_password;
    TextView tv_change_username;
    TextView tv_change_reward;
    TextView tv_change_member_since;
    TextView tv_change_email;
    Button btnLogout;
    TextView name_header;
    TextView email_header;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        tv_change_username = (TextView)findViewById(R.id.tv_change_username);
        tv_change_email = (TextView) findViewById(R.id.tv_change_email);

        //toolbars
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navdrawer

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
                tv_change_email.setText(data.getString(1));
                tv_change_username.setText(data.getString(0));
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

        //main Content
        card_username = (CardView) findViewById(R.id.card_username);
        card_email = (CardView) findViewById(R.id.card_email);
        card_password = (CardView) findViewById(R.id.card_password);
        tv_change_username = (TextView) findViewById(R.id.tv_change_username);
        tv_change_reward = (TextView) findViewById(R.id.tv_change_reward);
        tv_change_member_since = (TextView) findViewById(R.id.tv_change_member_since);
        tv_change_email = (TextView) findViewById(R.id.tv_change_email);
        btnLogout = (Button) findViewById(R.id.btn_logout);

        //OnclickListeners-Cards
        card_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogUsername();
            }
        });
        card_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDialogEmail();
            }
        });
        card_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPassword();
            }
        });

        //Reward and DateJoined
        String reward = activeIdPassing.getReward();
        tv_change_reward.setText(reward);
        databaseAccess.open();
        String dateJoined = databaseAccess.getDateJoined(id);
        tv_change_member_since.setText(dateJoined);

        //OnClickListeners-Button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Homepage.home.finish();
                databaseAccess.open();
                databaseAccess.doLogout();
                databaseAccess.close();
                startActivity(new Intent(AccountSetting.this, Login.class));
                finish();
            }
        });
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
    protected void onPause() {
        super.onPause();
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homepage_drw) {
            Intent homepageIntent = new Intent(this, Homepage.class);
            startActivity(homepageIntent);
        } else if (id == R.id.mycourse_drw) {
            Intent mycourseIntent = new Intent(this, MyCourses.class);
            startActivity(mycourseIntent);
        } else if (id == R.id.profile_drw) {
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
        } else if (id == R.id.accsett_drw) {
            //
        } else if (id == R.id.aboutus_drw) {
            Intent aboutusIntent = new Intent(this, AboutUs.class);
            startActivity(aboutusIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDialogUsername(){
        DialogUsername dialogUsername = new DialogUsername();
        dialogUsername.show(getSupportFragmentManager(), "Change Username");
    }
    public void openDialogEmail(){
        DialogEmail dialogEmail = new DialogEmail();
        dialogEmail.show(getSupportFragmentManager(), "Change Email");
    }
    public void openDialogPassword(){
        DialogPassword dialogPassword = new DialogPassword();
        dialogPassword.show(getSupportFragmentManager(), "Change Password");
    }

    @Override
    public void applyTextsUsername(String username) {
        tv_change_username.setText(username);
        name_header.setText(username);
    }

    @Override
    public void applyTextsEmail(String email) {
        tv_change_email.setText(email);
        email_header.setText(email);
    }
}
