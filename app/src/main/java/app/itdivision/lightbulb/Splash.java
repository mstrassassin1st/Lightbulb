package app.itdivision.lightbulb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //temp
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1.5s = 1500ms
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean firstStart = prefs.getBoolean("firstStart", true);

                if(firstStart){
                    Intent intent = new Intent(Splash.this, AppIntroActivity.class);
                    startActivity(intent);
                }
                else{
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    databaseAccess.open();
                    int id = databaseAccess.getHasSignedIn();
                    if(id > 0){
                        Intent intent = new Intent(Splash.this, Homepage.class);
                        startActivity(intent);
                        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                        activeIdPassing.setActiveId(id);

                        int ctr = 0;

                        ctr += databaseAccess.getCompletedCourse(id, 1,1);
                        ctr += databaseAccess.getCompletedCourse(id, 2,1);
                        ctr += databaseAccess.getCompletedCourse(id, 3,1);
                        ctr += databaseAccess.getCompletedCourse(id, 4,1);
                        ctr += databaseAccess.getCompletedCourse(id, 5,1);
                        ctr += databaseAccess.getCompletedCourse(id, 6,1);

                        String award = " ";
                        if(ctr <= 10){
                            award = "Bronze Medal";
                            activeIdPassing.setReward(award);
                        }else if( ctr <= 20){
                            award = "Silver Medal";
                            activeIdPassing.setReward(award);
                        }else{
                            award = "Gold Medal";
                            activeIdPassing.setReward(award);
                        }

                        databaseAccess.close();
                        finish();
                    }else{
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        }, 1500);

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
