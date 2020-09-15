package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 26-12-2018.
 */

public class AnswerView {

    String questionid;
    String question;
    String correctAnswer;
    String userAnswer;
    String description;

    public AnswerView(String questionid, String question, String correctAnswer, String userAnswer) {
        this.questionid = questionid;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
    }

    public AnswerView(String questionid, String question, String correctAnswer, String userAnswer, String description) {
        this.questionid = questionid;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
        this.description = description;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
