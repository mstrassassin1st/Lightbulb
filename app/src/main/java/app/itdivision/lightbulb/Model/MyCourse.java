package app.itdivision.lightbulb.Model;

public class MyCourse {

    private String courseName;
    private String courseCategory;
    private float coursePrice;
    private int thumbnail;
    private int isCompleted;

    public MyCourse(String courseTitle, String courseCategory, float coursePrice, int isCompleted) {
        this.courseName = courseTitle;
        this.courseCategory = courseCategory;
        this.coursePrice = coursePrice;
        this.isCompleted = isCompleted;
    }
    public MyCourse(String courseTitle, String courseCategory, float coursePrice, int thumbnail, int isCompleted) {
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

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int isCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }
}
