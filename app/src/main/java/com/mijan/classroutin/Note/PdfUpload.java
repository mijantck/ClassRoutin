package com.mijan.classroutin.Note;

public class PdfUpload {
    private  String PdfName;
    private  String PdfOthorName;
    private  String Date;
    private  String PdfOthorId;
    private  String ImageUrl;
    private  String PdfUrl;

    public  PdfUpload (){}

    public PdfUpload(String pdfName, String pdfOthorName, String date, String pdfOthorId, String imageUrl, String pdfUrl) {
        PdfName = pdfName;
        PdfOthorName = pdfOthorName;
        Date = date;
        PdfOthorId = pdfOthorId;
        ImageUrl = imageUrl;
        PdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return PdfName;
    }

    public void setPdfName(String pdfName) {
        PdfName = pdfName;
    }

    public String getPdfOthorName() {
        return PdfOthorName;
    }

    public void setPdfOthorName(String pdfOthorName) {
        PdfOthorName = pdfOthorName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPdfOthorId() {
        return PdfOthorId;
    }

    public void setPdfOthorId(String pdfOthorId) {
        PdfOthorId = pdfOthorId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPdfUrl() {
        return PdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        PdfUrl = pdfUrl;
    }
}
