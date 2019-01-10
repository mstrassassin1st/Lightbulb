package app.itdivision.lightbulb;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;

public class CurrentCoursePayment extends AppCompatActivity {

    ImageView imageCourse;
    TextView courseName;
    TextView courseCategory;
    TextView courseCreator;
    TextView courseReleaseDate;
    TextView courseRating;
    TextView BankName;
    TextView PaymentAmount;
    TextView BankAccNumber;
    TextView BankAccHolder;
    TextView AccHolderEmail;
    Button confirmPayment;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CurrentCoursePayment.this);
    int CourseID = 0;
    int FacilitatorID = 0;
    int CoursePrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_course_payment);

        imageCourse = findViewById(R.id.imgViewCurrent_course);
        courseName = findViewById(R.id.tv_course_name);
        courseCategory = findViewById(R.id.tv_course_category);
        courseCreator = findViewById(R.id.tv_course_creator);
        courseReleaseDate = findViewById(R.id.tv_course_releasedate);
        courseRating = findViewById(R.id.tv_course_rating);
        BankName = findViewById(R.id.tv_bank_type);
        PaymentAmount = findViewById(R.id.tv_payment_amount);
        BankAccNumber = findViewById(R.id.tv_bank_acc_num);
        BankAccHolder = findViewById(R.id.tv_bank_acc_name);
        AccHolderEmail = findViewById(R.id.tv_acc_holder_email);
        confirmPayment = findViewById(R.id.btn_confirm_payment);

        Intent intent = getIntent();
        String CourseTitle = intent.getStringExtra("CourseName");
        databaseAccess.open();
        try {
            String CourseName = "Empty";
            String CourseCategory = "Empty";
            String CourseCreator = "Empty";
            Cursor cursor = databaseAccess.getDetailCourse(CourseTitle);
            if(cursor.moveToFirst()){
                CourseName = cursor.getString(0);
                CourseCategory = cursor.getString(1);
                CourseCreator = cursor.getString(2);
                float CourseRating = cursor.getFloat(3);
                int CoursePrice = cursor.getInt(5);
                CourseID = cursor.getInt(6);

                courseName.setText(CourseName);
                courseCategory.setText(CourseCategory);
                String creator = "Creator: " + CourseCreator;
                courseCreator.setText(creator);
                String rating = Float.toString(CourseRating) + "/5.0";
                courseRating.setText(rating);
                String price = "IDR " + Integer.toString(CoursePrice);
                PaymentAmount.setText(price);
            }
            cursor.close();
            cursor = databaseAccess.getPaymentData(CourseCreator);

            try{
                if(cursor.moveToFirst()){
                    String FacilitatorName = cursor.getString(0);
                    String FacilitatorEmail = cursor.getString(1);
                    String FacilitatorBankAccount = cursor.getString(2);
                    FacilitatorID = cursor.getInt(3);

                    String accHolder = "a/n " + FacilitatorName;
                    AccHolderEmail.setText(FacilitatorEmail);
                    BankAccNumber.setText(FacilitatorBankAccount);
                    BankAccHolder.setText(accHolder);
                }
            }catch (Exception e){
                Toast.makeText(CurrentCoursePayment.this,"Error: "+e.toString(), Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        databaseAccess.close();
        confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                int id = activeIdPassing.getActiveId();
                databaseAccess.open();
                try {
                    databaseAccess.PaymentApproval(id, FacilitatorID, CourseID);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " +e.toString(), Toast.LENGTH_LONG).show();
                }
                try {
                    databaseAccess.PaymentAdd(FacilitatorID, CoursePrice);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " +e.toString(), Toast.LENGTH_LONG).show();
                }
                try {
                    databaseAccess.PurchaseCourse(id, FacilitatorID, CourseID);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " +e.toString(), Toast.LENGTH_LONG).show();
                }

                databaseAccess.close();
                Toast.makeText(CurrentCoursePayment.this, "Your course has been registered", Toast.LENGTH_LONG).show();
                Intent finish = new Intent(CurrentCoursePayment.this, MyCourses.class);
                startActivity(finish);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CurrentCoursePayment.this, Homepage.class);
        startActivity(intent);
        finish();
    }
}
