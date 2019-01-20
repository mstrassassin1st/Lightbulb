package app.itdivision.lightbulb.Model;

import android.graphics.Bitmap;

public class Course {
    private String courseTitle;
    private String courseCategory;
    private int coursePrice;
    private Bitmap thumbnail;

    public Course(String courseTitle, String courseCategory, int coursePrice) {
        this.courseTitle = courseTitle;
        this.courseCategory = courseCategory;
        this.coursePrice = coursePrice;
    }

    public Course(String courseTitle, String courseCategory, int coursePrice, Bitmap thumbnail) {
        this.courseTitle = courseTitle;
        this.courseCategory = courseCategory;
        this.coursePrice = coursePrice;
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }
}
