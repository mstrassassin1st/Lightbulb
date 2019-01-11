package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import app.itdivision.lightbulb.Database.DatabaseAccess;

public class CurrentLesson extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    YouTubePlayerSupportFragment yt_player;
    Button btn_play_lesson;
    YouTubePlayer.OnInitializedListener ytOnInitializedListener;
    TextView lessonName;
    TextView lessonDesc;
    TextView name_header;
    TextView email_header;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_lesson);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name_header = (TextView) headerView.findViewById(R.id.name_header_drw);
        email_header = (TextView) headerView.findViewById(R.id.email_header_drw);

        Intent intent = getIntent();
        int id = intent.getIntExtra("userID", 0);
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

        lessonName = (TextView)findViewById(R.id.tv_lesson_name);
        lessonDesc = (TextView)findViewById(R.id.tv_lesson_header_desc);

        String getName = getIntent().getStringExtra("LessonName");
        String getDesc = getIntent().getStringExtra("LessonDesc");

        databaseAccess.open();
        final String URL = databaseAccess.getLessonVideo(getName, getDesc);
        databaseAccess.close();

        lessonName.setText(getName);
        lessonDesc.setText(getDesc);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer + navdrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //ytplayer
        btn_play_lesson = (Button) findViewById(R.id.btn_play_lesson);
        yt_player = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.yt_player);
        ytOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(URL);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        btn_play_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yt_player.initialize(VideoPlayerConfig.getApiKey(), ytOnInitializedListener);

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
