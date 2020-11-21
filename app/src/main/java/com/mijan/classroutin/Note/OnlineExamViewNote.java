package com.mijan.classroutin.Note;

public class OnlineExamViewNote {

    private String id;
    private String examid;
    private String courCretorid;
    private String examType;
    private String examName;
    private String examNameCatagory;
    private String examDuretion;
    private String examMarks;
    private long  examStartDate;
    private long  examStartTime ;
    private long  examStopTime ;

    public OnlineExamViewNote(){}


    public OnlineExamViewNote(String id, String examid, String courCretorid, String examType,
                              String examName, String examNameCatagory, String examDuretion,
                              String examMarks, long examStartDate, long examStartTime, long examStopTime) {
        this.id = id;
        this.examid = examid;
        this.courCretorid = courCretorid;
        this.examType = examType;
        this.examName = examName;
        this.examNameCatagory = examNameCatagory;
        this.examDuretion = examDuretion;
        this.examMarks = examMarks;
        this.examStartDate = examStartDate;
        this.examStartTime = examStartTime;
        this.examStopTime = examStopTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getCourCretorid() {
        return courCretorid;
    }

    public void setCourCretorid(String courCretorid) {
        this.courCretorid = courCretorid;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamNameCatagory() {
        return examNameCatagory;
    }

    public void setExamNameCatagory(String examNameCatagory) {
        this.examNameCatagory = examNameCatagory;
    }

    public String getExamDuretion() {
        return examDuretion;
    }

    public void setExamDuretion(String examDuretion) {
        this.examDuretion = examDuretion;
    }

    public String getExamMarks() {
        return examMarks;
    }

    public void setExamMarks(String examMarks) {
        this.examMarks = examMarks;
    }

    public long getExamStartDate() {
        return examStartDate;
    }

    public void setExamStartDate(long examStartDate) {
        this.examStartDate = examStartDate;
    }

    public long getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(long examStartTime) {
        this.examStartTime = examStartTime;
    }

    public long getExamStopTime() {
        return examStopTime;
    }

    public void setExamStopTime(long examStopTime) {
        this.examStopTime = examStopTime;
    }
}
