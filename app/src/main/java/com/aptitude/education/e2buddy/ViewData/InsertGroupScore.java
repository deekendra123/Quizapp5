package com.aptitude.education.e2buddy.ViewData;

public class InsertGroupScore {

    int score;
    int question_answered;
    String student_name;


    public InsertGroupScore(int score, int question_answered, String student_name) {
        this.score = score;
        this.question_answered = question_answered;
        this.student_name = student_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestion_answered() {
        return question_answered;
    }

    public void setQuestion_answered(int question_answered) {
        this.question_answered = question_answered;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
