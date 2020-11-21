package com.mijan.classroutin.Note;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.List;

public class NoteRoutin {

    @Exclude
    private String id;

    private int serial;
    private String subject;
    private String roome;
    private String techer;
    private long start_time;
    private String end_time;

    // private String day_view;
    List<String> day;


    public NoteRoutin() {
        //empty constructor needed
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }


    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoome() {
        return roome;
    }

    public void setRoome(String roome) {
        this.roome = roome;
    }

    public String getTecher() {
        return techer;
    }

    public void setTecher(String techer) {
        this.techer = techer;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

   /* public String getDay_view() {
        return day_view;
    }*/

    public List<String> getDay() {
        return day;
    }

    public void setDay(List<String> day) {
        this.day = day;
    }

    /*public void setDay_view(String day_view) {
        this.day_view = day_view;
    }*/

    public NoteRoutin(int serial, String subject, String roome,
                      String techer, long start_time, String end_time, List<String> day) {


        this.serial = serial;
        this.subject = subject;
        this.roome = roome;
        this.techer = techer;
        this.start_time = start_time;
        this.end_time = end_time;
        //  this.day_view =day_view;
        this.day = day;

    }


}