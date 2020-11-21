package com.mijan.classroutin.Note;

public class NewsAddedNote {

    private String id;
    private String Cours_Code;
    private String Course_Name;
    private String Date;
    private long  Time;
    private String News_Feed;
    private String mImageUrl;
    private String pdfUrl;
    private String pdfName;
    private String PostOwner;
    private String PostOwnerURL;
    private String PostOwnerName;

   public  NewsAddedNote(){
    }

    public NewsAddedNote(String id, String cours_Code, String course_Name, String date, long time, String news_Feed ,String postOwner,String PostOwnerURL,String PostOwnerName) {
        this.id = id;
        this.Cours_Code = cours_Code;
        this.Course_Name = course_Name;
        this.Date = date;
        this.Time = time;
        this.News_Feed = news_Feed;
        this.PostOwner = postOwner;
        this.PostOwnerURL = PostOwnerURL;
        this.PostOwnerName = PostOwnerName;
    }

    public NewsAddedNote(String id, String cours_Code, String course_Name, String date, long time, String news_Feed, String mImageUrl,String postOwner,String PostOwnerURL,String PostOwnerName) {
        this.id = id;
        this.Cours_Code = cours_Code;
        this.Course_Name = course_Name;
        this.Date = date;
        this.Time = time;
        this.News_Feed = news_Feed;
        this.mImageUrl = mImageUrl;
        this.PostOwner = postOwner;
        this.PostOwnerURL = PostOwnerURL;
        this.PostOwnerName = PostOwnerName;
    }

    public NewsAddedNote(String id, String cours_Code, String course_Name, String date, long time, String news_Feed, String pdfUrl, String pdfName,String postOwner,String PostOwnerURL,String PostOwnerName) {
        this.id = id;
        this.Cours_Code = cours_Code;
        this.Course_Name = course_Name;
        this.Date = date;
        this.Time = time;
        this.News_Feed = news_Feed;
        this.pdfUrl = pdfUrl;
        this.pdfName = pdfName;
        this.PostOwner = postOwner;
        this.PostOwnerURL = PostOwnerURL;
        this.PostOwnerName = PostOwnerName;
    }

    public NewsAddedNote(String id, String cours_Code, String course_Name, String date, long time, String news_Feed, String mImageUrl, String pdfUrl, String pdfName,String postOwner,String PostOwnerURL,String PostOwnerName) {
        this.id = id;
        this.Cours_Code = cours_Code;
        this.Course_Name = course_Name;
        this.Date = date;
        this.Time = time;
        this.News_Feed = news_Feed;
        this.mImageUrl = mImageUrl;
        this.pdfUrl = pdfUrl;
        this.pdfName = pdfName;
        this.PostOwner = postOwner;
        this.PostOwnerURL = PostOwnerURL;
        this.PostOwnerName = PostOwnerName;
    }

    public String getPostOwnerName() {
        return PostOwnerName;
    }

    public void setPostOwnerName(String postOwnerName) {
        PostOwnerName = postOwnerName;
    }

    public String getPostOwnerURL() {
        return PostOwnerURL;
    }

    public void setPostOwnerURL(String postOwnerURL) {
        PostOwnerURL = postOwnerURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCours_Code() {
        return Cours_Code;
    }

    public void setCours_Code(String cours_Code) {
        Cours_Code = cours_Code;
    }

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getNews_Feed() {
        return News_Feed;
    }

    public void setNews_Feed(String news_Feed) {
        News_Feed = news_Feed;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPostOwner() {
        return PostOwner;
    }

    public void setPostOwner(String postOwner) {
        PostOwner = postOwner;
    }
}
