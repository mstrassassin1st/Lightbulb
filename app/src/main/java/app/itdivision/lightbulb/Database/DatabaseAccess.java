package app.itdivision.lightbulb.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(db != null){
            this.db.close();
        }
    }

    //Query hasSignedIn
    public int getHasSignedIn(){
        Cursor cursor = db.rawQuery("SELECT StudentID from UserSignedIn", null);
        int id = 0;
        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("StudentID"));
        }
        return id;
    }

    //Query to fill hasSignedIn
    public void setHasSignedIn(int id){
        db.execSQL("update UserSignedIn set StudentID = '"+ id +"'");
    }
    public void doLogout(){
        db.execSQL("UPDATE UserSignedIn SET StudentID = 0");
    }

    //Query for Login
    public int getLogin(String email, String password){
        Cursor cursor = db.rawQuery("select StudentID from MsStudent where StudentEmail = '"+ email +"' AND StudentPassword = '"+ password +"'", null);
        int id = 0;
        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex("StudentID"));
        }
        return id;
    }

    //Get header data
    public Cursor getStudentData(String id){
        Cursor cursor = db.rawQuery("select StudentName, StudentEmail from MsStudent where StudentID LIKE '" + id + "'", null);
        return cursor;
    }

    //Query for Register
    public void getRegistered (String username, String email, String password){
        db.execSQL("insert into MsStudent(StudentName, StudentEmail, StudentPassword, StudentHint)\n" +
                "values ('"+ username +"','"+ email + "','" + password + "','"+ password +"')");
    }


    //Query for Courses
    public Cursor getCourses(){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, CoursePrice from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID", null);
        return courses;
    }
    public Cursor getDetailCourse(String CourseName){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, FacilitatorName, CourseRating, CourseDescription, CoursePrice, CourseID  from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID JOIN MsFacilitator mf ON mc.FacilitatorID = mf.FacilitatorID where CourseName = '"+ CourseName +"'", null);
        return courses;
    }

    public Cursor getCustomCourses(int id){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, CoursePrice from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID where mc.CourseTypeId = '"+ id +"'", null);
        return courses;
    }


    //Query for Lesson
    public Cursor getLesson(int id){
        Cursor lessons = db.rawQuery("select ModuleName, ModuleDescription from Module_of_Course where CourseID = '"+ id +"'",null);
        return lessons;
    }

    //Query for MyCourses
    public Cursor getMyCourses(int id){
        Cursor cursor = db.rawQuery("select CourseName, CourseTypeName, CourseStatus from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID = '" + id + "'", null);
        return cursor;
    }
    public Cursor getMyCoursesDetail(int id, int status){
        Cursor cursor = db.rawQuery("select CourseName, CourseTypeName, CourseStatus from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID ='" + id + "' AND CourseStatus = '" + status + "'", null);
        return cursor;
    }
    public int checkCourse(int StudentId, int CourseId, int Status ){
        Cursor cursor = db.rawQuery("select count(*) from PurchaseCourse where StudentID = '"+ StudentId +"' AND CourseID =  '" + CourseId +"' AND PaymentStatus = '1'", null);
        int res = 0;
        if(cursor.moveToFirst()){
            res = cursor.getInt(0);
        }
        return res;
    }

    //Query for CoursePayment
    public Cursor getPaymentData(String facilitatorName){
        Cursor cursor = db.rawQuery("select FacilitatorName, FacilitatorEmail, FacilitatorBankAccount, FacilitatorId from MsFacilitator where FacilitatorName LIKE '"+ facilitatorName +"'", null);
        return cursor;
    }
    public void PaymentApproval(int StudentID, int FacilitatorID, int CourseID){
        db.execSQL("insert into Student_on_course(StudentID, FacilitatorID, CourseID, CourseStatus, RatingByStudent) VALUES ('"+ StudentID +"', '"+ FacilitatorID +"', '"+ CourseID +"', '0', '0.0')");
    }
    public void PaymentAdd(int FacilitatorID, int price){
        db.execSQL("update MsFacilitator set FacilitatorBalance = FacilitatorBalance + '"+ price +"' where FacilitatorID = '" + FacilitatorID +"'");
    }
    public void PurchaseCourse(int Student, int Facilitator, int CourseID){
        db.execSQL("insert into PurchaseCourse(StudentID, FacilitatorID, CourseID, PaymentStatus) values ('"+ Student +"', '"+ Facilitator +"', '"+ CourseID +"', '1')");
    }




}
