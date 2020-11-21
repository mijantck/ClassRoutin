package com.mijan.classroutin.Note;

import android.widget.EditText;
import android.widget.TextView;

public class Exam {

    private  String ExamName;
    private  String ExamTopic;
    private  String ExamDate;
    private  String ExamTime;
    private  String ExamSubject;
    private  String ExamHoll;
    private  String ExamCode;

    public Exam(){}



    public Exam(String examName, String examTopic, String examDate, String examTime, String examSubject,String examCode, String examHoll) {
        ExamName = examName;
        ExamTopic = examTopic;
        ExamDate = examDate;
        ExamTime = examTime;
        ExamSubject = examSubject;
        ExamHoll = examHoll;
        ExamCode = examCode;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String examName) {
        ExamName = examName;
    }

    public String getExamTopic() {
        return ExamTopic;
    }

    public void setExamTopic(String examTopic) {
        ExamTopic = examTopic;
    }

    public String getExamDate() {
        return ExamDate;
    }

    public void setExamDate(String examDate) {
        ExamDate = examDate;
    }

    public String getExamTime() {
        return ExamTime;
    }

    public void setExamTime(String examTim) {
        ExamTime = examTim;
    }

    public String getExamSubject() {
        return ExamSubject;
    }

    public void setExamSubject(String examSubject) {
        ExamSubject = examSubject;
    }

    public String getExamHoll() {
        return ExamHoll;
    }

    public void setExamHoll(String examHoll) {
        ExamHoll = examHoll;
    }

    public String getExamCode() {
        return ExamCode;
    }

    public void setExamCode(String examCode) {
        ExamCode = examCode;
    }
}
