package com.mijan.classroutin.Note;

public class ExaminnerInfoNote {
    private String id;
    private String name;
    private String rolNumber;
    private int marks;

    public ExaminnerInfoNote(){}


    public ExaminnerInfoNote(String id, String name, String rolNumber, int marks) {
        this.id = id;
        this.name = name;
        this.rolNumber = rolNumber;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRolNumber() {
        return rolNumber;
    }

    public void setRolNumber(String rolNumber) {
        this.rolNumber = rolNumber;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
