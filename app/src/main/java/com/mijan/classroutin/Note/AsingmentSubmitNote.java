package com.mijan.classroutin.Note;

public class AsingmentSubmitNote {
    private String id;
    private String uid;
    private String name;
    private int rol;
    private int page;
    private String imageURL;
    private String date;
    private long endTime;

    public AsingmentSubmitNote(){}

    public AsingmentSubmitNote(String id, String uid, String name, int page, String imageURL, String date, long endTime) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.page = page;
        this.imageURL = imageURL;
        this.date = date;
        this.endTime = endTime;
    }

    public AsingmentSubmitNote(String id, String uid, String name, int rol, int page, String imageURL) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.rol = rol;
        this.page = page;
        this.imageURL = imageURL;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
