package com.mijan.classroutin.Note;

public class ParticifentNote {
    private  String id;
    private  String coresId;
    private String uID;
    private String uName;
    private String uRolNumber;
    private String usection;
    private String uEmail;
    private String uURL;
    private int marks;


    public ParticifentNote (){

    }

    public ParticifentNote(String id, String coresId, String uID, String uName, String uEmail, String uURL) {
        this.id = id;
        this.coresId = coresId;
        this.uID = uID;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uURL = uURL;
    }

    public ParticifentNote(String coresId, String uID, String uName, String uRolNumber, String usection,
                           String uEmail, String uURL,int marks) {
        this.coresId = coresId;
        this.uID = uID;
        this.uName = uName;
        this.uRolNumber = uRolNumber;
        this.usection = usection;
        this.uEmail = uEmail;
        this.uURL = uURL;
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getuRolNumber() {
        return uRolNumber;
    }

    public void setuRolNumber(String uRolNumber) {
        this.uRolNumber = uRolNumber;
    }

    public String getUsection() {
        return usection;
    }

    public void setUsection(String usection) {
        this.usection = usection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoresId() {
        return coresId;
    }

    public void setCoresId(String coresId) {
        this.coresId = coresId;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuURL() {
        return uURL;
    }

    public void setuURL(String uURL) {
        this.uURL = uURL;
    }
}
