package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 21-01-2019.
 */

public class QuizCreditView {
    String quiz;
    String crdit;
    String date;

    public QuizCreditView(String quiz, String crdit, String date) {
        this.quiz = quiz;
        this.crdit = crdit;
        this.date = date;
    }


    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getCrdit() {
        return crdit;
    }

    public void setCrdit(String crdit) {
        this.crdit = crdit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
