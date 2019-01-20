package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.itdivision.lightbulb.Adapter.CurrentCourseRecyclerViewAdapter;
import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class CourseRating extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner rating;
    Button submit;
    TextView name;
    TextView creator;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    int finalRate = 0;
    String CourseCategory = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_rating);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView) findViewById(R.id.rate_courseName);
        creator = (TextView) findViewById(R.id.rate_courseCreator);

        rating = (Spinner) findViewById(R.id.sp_rating);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rating, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(adapter);
        rating.setOnItemSelectedListener(this);

        final String CourseName = getIntent().getStringExtra("CourseName");
        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
        final int stID = activeIdPassing.getActiveId();
        String CourseCreator = " ";
        databaseAccess.open();
        final Cursor cursor = databaseAccess.getDetailCourse(CourseName);
        if(cursor.moveToFirst()){
            CourseCreator = cursor.getString(2);
            CourseCategory = cursor.getString(1);

            name.setText(CourseName);
            creator.setText(CourseCreator);
        }
        databaseAccess.close();
        submit = (Button)findViewById(R.id.btn_submitRating);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalRate > 0){
                    databaseAccess.open();
                    int cId = databaseAccess.getCourseID(CourseName, CourseCategory);
                    databaseAccess.setCourseRate(cId, stID, finalRate);
                    ArrayList<Float> rate = new ArrayList<>();
                    Cursor cursor1 = databaseAccess.getRatingFromStudents(cId);
                    while (cursor1.moveToNext()){
                        rate.add(cursor1.getFloat(0));
                    }
                    float sumRate = 0;
                    float datactr = 0;
                    for(int i = 0; i < rate.size(); i++){
                        if(rate.get(i) == 0){

                        }else{
                            sumRate += rate.get(i);
                            datactr++;
                        }
                    }
                    float newCourseRate = sumRate/datactr;
                    databaseAccess.setNewCourseRating(cId, newCourseRate);
                    databaseAccess.close();
                    showToast();
                    finish();
                }else {
                    Toast.makeText(CourseRating.this, "Please choose rating value!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String rate = parent.getItemAtPosition(position).toString();
        switch (rate) {
            case "1":
                finalRate = 1;
                break;
            case "2":
                finalRate = 2;
                break;
            case "3":
                finalRate = 3;
                break;
            case "4":
                finalRate = 4;
                break;
            case "5":
                finalRate = 5;
                break;
            default:
                Toast.makeText(this, "Select Rating Value", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastImage.setImageResource(R.drawable.ic_check_black_24dp);
        toastText.setText("Thank you for your feedback!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
