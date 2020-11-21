package com.mijan.classroutin.Note;

public class Note {
    private String title;
    private String description;
    private String last_date;
    private int priority;



    public Note() {
        //empty constructor needed
    }

    public Note(String title, String description, int priority,String ladt_date) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.last_date = ladt_date;
    }


    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    }