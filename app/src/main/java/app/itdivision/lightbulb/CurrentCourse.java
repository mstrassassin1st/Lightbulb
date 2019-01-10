package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import app.itdivision.lightbulb.Adapter.CurrentCourseRecyclerViewAdapter;
import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;
import app.itdivision.lightbulb.Model.Lesson;

public class CurrentCourse extends AppCompatActivity {

    ImageView imgViewCurrentCourse;
    TextView tv_course_name;
    TextView tv_course_category;
    TextView tv_course_creator;
    TextView tv_course_releasedate;
    TextView tv_course_rating;
    TextView tv_course_description;
    RecyclerView recyclerCurrentLesson;
    Button btn_get_this_course;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CurrentCourse.this);
    List<Lesson> lessonList;
    boolean isBought = false;
    int CourseID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_course);

        imgViewCurrentCourse = (ImageView) findViewById(R.id.imgViewCurrent_course);
        tv_course_name = (TextView) findViewById(R.id.tv_course_name);
        tv_course_category = (TextView) findViewById(R.id.tv_course_category);
        tv_course_creator = (TextView) findViewById(R.id.tv_course_creator);
        tv_course_releasedate = (TextView) findViewById(R.id.tv_course_releasedate);
        tv_course_rating = (TextView)findViewById(R.id.tv_course_rating);
        tv_course_description = (TextView) findViewById(R.id.course_description);
        recyclerCurrentLesson = (RecyclerView) findViewById(R.id.recyclerCurrentLesson);
        btn_get_this_course = (Button) findViewById(R.id.btn_get_this_course);

        //getIntent
        final String CourseTitle = getIntent().getStringExtra("CourseTitle");
        databaseAccess.open();
        Cursor cursor = databaseAccess.getDetailCourse(CourseTitle);
        try{
            if(cursor.moveToFirst()){
                String CourseName = cursor.getString(0);
                String CourseCategory = cursor.getString(1);
                String CourseCreator = cursor.getString(2);
                float CourseRating = cursor.getFloat(3);
                String CourseDescription = cursor.getString(4);
                int CoursePrice = cursor.getInt(5);
                CourseID = cursor.getInt(6);

                tv_course_name.setText(CourseName);
                tv_course_category.setText(CourseCategory);
                String creator = "Creator: " + CourseCreator;
                tv_course_creator.setText(creator);
                String rating = Float.toString(CourseRating) + "/5.0";
                tv_course_rating.setText(rating);
                tv_course_description.setText(CourseDescription);
                String price = "GET THIS COURSE: IDR" + CoursePrice;
                btn_get_this_course.setText(price);
            }
            cursor.close();
        }catch (Exception e){

        }
        databaseAccess.close();

        databaseAccess.open();
        Cursor cursor1 = databaseAccess.getLesson(CourseID);
        lessonList = new ArrayList<>();
        try{
            while(cursor1.moveToNext()){
                String lessonName = cursor1.getString(cursor1.getColumnIndex("ModuleName"));
                String lessonDesc = cursor1.getString(cursor1.getColumnIndex("ModuleDescription"));
                Lesson lesson = new Lesson(lessonName, lessonDesc);
                lessonList.add(lesson);
            }
        }catch (Exception e){

        }
        cursor1.close();
        databaseAccess.close();

        CurrentCourseRecyclerViewAdapter adapter = new CurrentCourseRecyclerViewAdapter(this, lessonList);
        recyclerCurrentLesson.setLayoutManager(new LinearLayoutManager(this));
        recyclerCurrentLesson.setAdapter(adapter);

        //isBought = true;
        databaseAccess.open();
        ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
        int id = activeIdPassing.getActiveId();
        int valid = databaseAccess.checkCourse(id, CourseID, 1);
        if(valid > 0) {
            isBought = true;
        }
        databaseAccess.close();

        if(isBought){
            btn_get_this_course.setVisibility(View.GONE);
            recyclerCurrentLesson.setVisibility(View.VISIBLE);
        }else{
            btn_get_this_course.setVisibility(View.VISIBLE);
            recyclerCurrentLesson.setVisibility(View.GONE);
            btn_get_this_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentCourse.this, CurrentCoursePayment.class);
                    intent.putExtra("CourseName", CourseTitle);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
