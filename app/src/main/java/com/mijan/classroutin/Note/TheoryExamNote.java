package com.mijan.classroutin.Note;

public class TheoryExamNote {
    private String thisExamPostID;
    private String thisExamID;
    private String thisExamCourseID;
    private String thisExamCourseCreatorID;
    private String TheoryExamSubjectName;
    private int TheoryExamSubjectPageNumer;
    private String TheoryExamSubjectImageURL;


    public TheoryExamNote(){}

    public TheoryExamNote(String thisExamPostID, String thisExamID, String thisExamCourseID,
                          String thisExamCourseCreatorID, String theoryExamSubjectName,
                          int theoryExamSubjectPageNumer, String theoryExamSubjectImageURL) {
        this.thisExamPostID = thisExamPostID;
        this.thisExamID = thisExamID;
        this.thisExamCourseID = thisExamCourseID;
        this.thisExamCourseCreatorID = thisExamCourseCreatorID;
        TheoryExamSubjectName = theoryExamSubjectName;
        TheoryExamSubjectPageNumer = theoryExamSubjectPageNumer;
        TheoryExamSubjectImageURL = theoryExamSubjectImageURL;
    }

    public String getThisExamPostID() {
        return thisExamPostID;
    }

    public void setThisExamPostID(String thisExamPostID) {
        this.thisExamPostID = thisExamPostID;
    }

    public String getThisExamID() {
        return thisExamID;
    }

    public void setThisExamID(String thisExamID) {
        this.thisExamID = thisExamID;
    }

    public String getThisExamCourseID() {
        return thisExamCourseID;
    }

    public void setThisExamCourseID(String thisExamCourseID) {
        this.thisExamCourseID = thisExamCourseID;
    }

    public String getThisExamCourseCreatorID() {
        return thisExamCourseCreatorID;
    }

    public void setThisExamCourseCreatorID(String thisExamCourseCreatorID) {
        this.thisExamCourseCreatorID = thisExamCourseCreatorID;
    }

    public String getTheoryExamSubjectName() {
        return TheoryExamSubjectName;
    }

    public void setTheoryExamSubjectName(String theoryExamSubjectName) {
        TheoryExamSubjectName = theoryExamSubjectName;
    }

    public int getTheoryExamSubjectPageNumer() {
        return TheoryExamSubjectPageNumer;
    }

    public void setTheoryExamSubjectPageNumer(int theoryExamSubjectPageNumer) {
        TheoryExamSubjectPageNumer = theoryExamSubjectPageNumer;
    }

    public String getTheoryExamSubjectImageURL() {
        return TheoryExamSubjectImageURL;
    }

    public void setTheoryExamSubjectImageURL(String theoryExamSubjectImageURL) {
        TheoryExamSubjectImageURL = theoryExamSubjectImageURL;
    }
}
