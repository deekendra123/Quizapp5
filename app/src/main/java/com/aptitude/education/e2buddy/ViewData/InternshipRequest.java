package com.aptitude.education.e2buddy.ViewData;

public class InternshipRequest {

    String studentName, studentEmail, studentNumber, course, duration, image, status, dateFrom, dateTo;

    public InternshipRequest(String studentName, String studentEmail, String studentNumber, String course, String duration, String image, String status, String dateFrom, String dateTo) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentNumber = studentNumber;
        this.course = course;
        this.duration = duration;
        this.image = image;
        this.status = status;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
