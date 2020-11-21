package com.mijan.classroutin.Note;

public class MCQQuastionNote {
   private int quaNo;
   private String id;
   private String examid;
   private String examCeatoreID;
   private String quastionn;
   private String ansA;
   private String ansAdiscription;
   private String ansB;
   private String ansBdiscription;
   private String ansC;
   private String ansCdiscription;
   private String ansD;
   private String ansDdiscription;
   private String curectans;
   private String curectansSubmitedByStuden;
   private long endTime;
   private long startTime;

   public MCQQuastionNote(){}

    public MCQQuastionNote(int quaNo, String id, String examid, String examCeatoreID, String quastionn,
                           String ansA, String ansAdiscription, String ansB, String ansBdiscription,
                           String ansC, String ansCdiscription, String ansD, String ansDdiscription,
                           String curectans, long endTime, long startTime) {
        this.quaNo = quaNo;
        this.id = id;
        this.examid = examid;
        this.examCeatoreID = examCeatoreID;
        this.quastionn = quastionn;
        this.ansA = ansA;
        this.ansAdiscription = ansAdiscription;
        this.ansB = ansB;
        this.ansBdiscription = ansBdiscription;
        this.ansC = ansC;
        this.ansCdiscription = ansCdiscription;
        this.ansD = ansD;
        this.ansDdiscription = ansDdiscription;
        this.curectans = curectans;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public MCQQuastionNote(int quaNo, String id, String examid, String examCeatoreID, String quastionn
            , String ansAdiscription, String ansBdiscription, String ansCdiscription, String ansDdiscription
            , String curectans, String curectansSubmitedByStuden) {
        this.quaNo = quaNo;
        this.id = id;
        this.examid = examid;
        this.examCeatoreID = examCeatoreID;
        this.quastionn = quastionn;
        this.ansAdiscription = ansAdiscription;
        this.ansBdiscription = ansBdiscription;
        this.ansCdiscription = ansCdiscription;
        this.ansDdiscription = ansDdiscription;
        this.curectans = curectans;
        this.curectansSubmitedByStuden = curectansSubmitedByStuden;
    }

    public String getCurectansSubmitedByStuden() {
        return curectansSubmitedByStuden;
    }

    public void setCurectansSubmitedByStuden(String curectansSubmitedByStuden) {
        this.curectansSubmitedByStuden = curectansSubmitedByStuden;
    }

    public int getQuaNo() {
        return quaNo;
    }

    public void setQuaNo(int quaNo) {
        this.quaNo = quaNo;
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

    public String getExamCeatoreID() {
        return examCeatoreID;
    }

    public void setExamCeatoreID(String examCeatoreID) {
        this.examCeatoreID = examCeatoreID;
    }

    public String getQuastionn() {
        return quastionn;
    }

    public void setQuastionn(String quastionn) {
        this.quastionn = quastionn;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsAdiscription() {
        return ansAdiscription;
    }

    public void setAnsAdiscription(String ansAdiscription) {
        this.ansAdiscription = ansAdiscription;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsBdiscription() {
        return ansBdiscription;
    }

    public void setAnsBdiscription(String ansBdiscription) {
        this.ansBdiscription = ansBdiscription;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsCdiscription() {
        return ansCdiscription;
    }

    public void setAnsCdiscription(String ansCdiscription) {
        this.ansCdiscription = ansCdiscription;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getAnsDdiscription() {
        return ansDdiscription;
    }

    public void setAnsDdiscription(String ansDdiscription) {
        this.ansDdiscription = ansDdiscription;
    }

    public String getCurectans() {
        return curectans;
    }

    public void setCurectans(String curectans) {
        this.curectans = curectans;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
