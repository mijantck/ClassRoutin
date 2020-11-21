package com.mijan.classroutin.Note;

public class Course_Note {

    private String randomSerchCode;
    private String CourseID;
    private String courseCode;
    private String courseName;
    private String section;
    private String teacher_name;
    private boolean access;
    private String CourseCreator;


    public Course_Note() {
        //empty constructor needed
    }

    public Course_Note(String courseCode, String courseName,String section, String teacher_name,String CourseCreator,boolean access) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.section = section;
        this.teacher_name = teacher_name;
        this.access = access;
        this.CourseCreator = CourseCreator;
    }

    public Course_Note(String randomSerchCode,String CourseID, String courseCode, String courseName,String section, String teacher_name,String CourseCreator,boolean access) {
        this.randomSerchCode = randomSerchCode;
        this.CourseID = CourseID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.section =section;
        this.teacher_name = teacher_name;
        this.access = access;
        this.CourseCreator = CourseCreator;
    }

    public String getRandomSerchCode() {
        return randomSerchCode;
    }

    public void setRandomSerchCode(String randomSerchCode) {
        this.randomSerchCode = randomSerchCode;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getCourseCreator() {
        return CourseCreator;
    }

    public void setCourseCreator(String courseCreator) {
        CourseCreator = courseCreator;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}