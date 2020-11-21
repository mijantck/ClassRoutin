package com.mijan.classroutin.Note;

public class AttendenceNote {
    private String id;
    private String courseID;
    private String uID;
    private String name;
    private int date;
    private String classOnSatrt;
    private String student;
    private String attend;
    private String studentURL;
    private String dateS;
    private int rol;
    private String email;

    public AttendenceNote(){}

    public AttendenceNote(String id, String courseID, String name, int date, String classOnSatrt,String dateS,String student) {
        this.id = id;
        this.courseID = courseID;
        this.name = name;
        this.date = date;
        this.classOnSatrt = classOnSatrt;
        this.dateS = dateS;
        this.student = student;
    }

    public AttendenceNote(String id, String courseID,String uID, String name, int date,String student
            ,String attend,String studentURL,String dateS,int rol,String email) {
        this.id = id;
        this.courseID = courseID;
        this.uID = uID;
        this.name = name;
        this.date = date;
        this.student = student;
        this.attend = attend;
        this.studentURL = studentURL;
        this.dateS = dateS;
        this.rol = rol;
        this.email = email;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getDateS() {
        return dateS;
    }

    public void setDateS(String dateS) {
        this.dateS = dateS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getClassOnSatrt() {
        return classOnSatrt;
    }

    public void setClassOnSatrt(String classOnSatrt) {
        this.classOnSatrt = classOnSatrt;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend = attend;
    }

    public String getStudentURL() {
        return studentURL;
    }

    public void setStudentURL(String studentURL) {
        this.studentURL = studentURL;
    }
}
