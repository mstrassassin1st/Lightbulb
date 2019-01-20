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

    //Query for Register
    public int checkEmail(String email){
        Cursor cursor = db.rawQuery("select count(StudentEmail) from MsStudent where StudentEmail = '"+ email +"'", null);
        int chck = 0;
        if(cursor.moveToFirst()){
            chck = cursor.getInt(0);
        }
        return chck;
    }

    public void getRegistered (String username, String email, String password, String dateJoined){
        db.execSQL("insert into MsStudent(StudentName, StudentEmail, StudentPassword, StudentHint, StudentDateJoined)\n" +
                "values ('"+ username +"','"+ email + "','" + password + "','"+ password +"', '"+ dateJoined +"')");
    }

    //Get header data
    public Cursor getStudentData(String id){
        Cursor cursor = db.rawQuery("select StudentName, StudentEmail from MsStudent where StudentID LIKE '" + id + "'", null);
        return cursor;
    }




    //Query for Courses
    public Cursor getCourses(){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, CoursePrice, CourseImage from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID", null);
        return courses;
    }
    public Cursor getDetailCourse(String CourseName){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, FacilitatorName, CourseRating, CourseDescription, CoursePrice, CourseID, CourseImage, CourseLaunchDate from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID JOIN MsFacilitator mf ON mc.FacilitatorID = mf.FacilitatorID where CourseName = '"+ CourseName +"'", null);
        return courses;
    }

    public Cursor getCustomCourses(int id){
        Cursor courses = db.rawQuery("select CourseName, CourseTypeName, CoursePrice, CourseImage from MsCourse mc JOIN MsCourseType mct ON mc.CourseTypeID = mct.CourseTypeID where mc.CourseTypeId = '"+ id +"'", null);
        return courses;
    }


    //Query for Lesson
    public Cursor getLesson(int id){
        Cursor lessons = db.rawQuery("select ModuleName, ModuleDescription from Module_of_Course where CourseID = '"+ id +"'",null);
        return lessons;
    }

    //Query for MyCourses
    public Cursor getMyCourses(int id){
        Cursor cursor = db.rawQuery("select CourseName, CourseTypeName, CourseStatus, CoursePrice ,CourseImage from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID = '" + id + "'", null);
        return cursor;
    }
    public Cursor getMyCoursesDetail(int id, int status){
        Cursor cursor = db.rawQuery("select CourseName, CourseTypeName, CourseStatus, CoursePrice, CourseImage from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID ='" + id + "' AND CourseStatus = '" + status + "'", null);
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
        db.execSQL("update MsFacilitator set FacilitatorBalance = FacilitatorBalance + "+ price +" where FacilitatorID = '" + FacilitatorID +"'");
    }
    public void PurchaseCourse(int Student, int Facilitator, int CourseID){
        db.execSQL("insert into PurchaseCourse(StudentID, FacilitatorID, CourseID, PaymentStatus) values ('"+ Student +"', '"+ Facilitator +"', '"+ CourseID +"', '1')");
    }

    //Query for Profile
    public int getPurchasedCourse(int StudentId, int CourseTypeId){
        Cursor cursor = db.rawQuery("select count(*) from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID = '"+ StudentId +"' AND B.CourseTypeID = '"+ CourseTypeId +"'", null);
        int count = 0;
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        return count;
    }

    public int getCompletedCourse(int StudentId, int CourseTypeId, int compStat){
        Cursor cursor = db.rawQuery("select count(*) from Student_on_Course A JOIN MsCourse B ON A.CourseID = B.CourseID JOIN MsCourseType C ON B.CourseTypeID = C.CourseTypeID where StudentID = '"+ StudentId +"' AND B.CourseTypeID = '"+ CourseTypeId +"' AND CourseStatus = '"+ compStat +"'", null);
        int count = 0;
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        return count;
    }

    //Query for AccSettings
    public String getDateJoined(int StudentID){
        Cursor cursor = db.rawQuery("select StudentDateJoined from MsStudent where StudentID = '" + StudentID +"'", null);
        String dateJoined = " ";
        if(cursor.moveToFirst()){
            dateJoined = cursor.getString(0);
        }
        return dateJoined;
    }

    public String getOldPassword(int StudentID){
        Cursor cursor = db.rawQuery("select StudentPassword from MsStudent where StudentID = '" + StudentID +"'", null);
        String passw = " ";
        if(cursor.moveToFirst()){
            passw = cursor.getString(0);
        }
        return passw;
    }

    public void changeEmail(String email, int id){
        db.execSQL("update MsStudent set StudentEmail = '" + email +"' where StudentID = '"+ id +"'");
    }

    public void changeUsername(String username, int id){
        db.execSQL("update MsStudent set StudentName = '" + username +"' where StudentID = '"+ id +"'");
    }

    public void changePassword(String password, int id){
        db.execSQL("update MsStudent set StudentPassword = '" + password +"' where StudentID = '"+ id +"'");
    }

    //Query for videos
    public String getLessonVideo(String LessonName, String LessonDesc){
        Cursor cursor = db.rawQuery("select ModuleURL from Module_of_Course where ModuleName = '" + LessonName +"' AND ModuleDescription = '" + LessonDesc +"'", null);
        String URL = " ";
        if(cursor.moveToFirst()){
            URL = cursor.getString(0);
        }
        return URL;
    }
    public String getPreviewVideo(String CourseName){
        Cursor cursor = db.rawQuery("select ModuleURL\n" +
                "from Module_of_Course A \n" +
                "\t JOIN MsCourse B ON A.CourseID = B.CourseID\n" +
                "where CourseName = '"+ CourseName +"' AND ModuleName = 'Lesson 1'", null);
        String URL = " ";
        if(cursor.moveToFirst()){
            URL = cursor.getString(0);
        }
        return URL;
    }

    //Rating
    public void setCourseRate(int CId, int stID, int rate){
        db.execSQL("UPDATE Student_on_Course SET RatingByStudent = '"+ rate +"' WHERE CourseID = '"+ CId +"' AND StudentID = '"+ stID + "'");
    }

    public Cursor getRatingFromStudents(int CId){
        Cursor cursor = db.rawQuery("SELECT RatingByStudent FROM Student_on_Course WHERE CourseID = '"+ CId +"'", null);
        return cursor;
    }

    public int getCourseID(String name, String category){
        String q = "select CourseID from MsCourse A JOIN MsCourseType B ON A.CourseTypeID = B.CourseTypeID where CourseName = '"+ name +"' and CourseTypeName = '"+ category +"'";
        Cursor cursor = db.rawQuery(q, null);
        int result = 0;
        if(cursor.moveToFirst()){
            result = cursor.getInt(0);
        }
        return result;
    }
    public void setNewCourseRating(int CId, float rating){
        db.execSQL("UPDATE MsCourse SET CourseRating = '"+ rating +"' WHERE CourseID = '"+ CId +"'");
    }

    //Set Course as Completed
    public void setCourseAsCompleted(int cId, int stID){
        db.execSQL("UPDATE Student_on_Course SET CourseStatus = '1' WHERE StudentID = '"+ stID +"' AND CourseID = '"+ cId + "'");
    }

    //Get Facilitator Balance
    public int getFacilBalance(String name){
        Cursor cursor = db.rawQuery("select FacilitatorBalance from MsFacilitator where FacilitatorName = '"+ name +"'", null);
        int bal = 0;
        if(cursor.moveToFirst()){
            bal = cursor.getInt(0);
        }
        return bal;
    }
}
