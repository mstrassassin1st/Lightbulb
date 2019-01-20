package app.itdivision.lightbulb.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MyCourse {

    private String courseName;
    private String courseCategory;
    private float coursePrice;
    private Bitmap thumbnail;
    private int isCompleted;

    public MyCourse(String courseTitle, String courseCategory, float coursePrice, int isCompleted) {
        this.courseName = courseTitle;
        this.courseCategory = courseCategory;
        this.coursePrice = coursePrice;
        this.isCompleted = isCompleted;
    }
    public MyCourse(String courseTitle, String courseCategory, float coursePrice, Bitmap thumbnail, int isCompleted) {
        this.courseName = courseTitle;
        this.courseCategory = courseCategory;
        this.coursePrice = coursePrice;
        this.thumbnail = thumbnail;
        this.isCompleted = isCompleted;
    }

    public MyCourse(String courseName, String courseCategory, int isCompleted) {
        this.courseName = courseName;
        this.courseCategory = courseCategory;
        this.isCompleted = isCompleted;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public float getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(float coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int isCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }
}
