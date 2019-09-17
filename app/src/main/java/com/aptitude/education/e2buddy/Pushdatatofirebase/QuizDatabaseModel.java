package com.aptitude.education.e2buddy.Pushdatatofirebase;

/**
 * Created by Matrix on 18-01-2019.
 */

public class QuizDatabaseModel {
    public QuizDatabaseModel() {
    }

    String quizDate,questionId, quizId , question, option1,option4,option3,option2, answer, description;

    public QuizDatabaseModel(String quizDate, String questionId, String quizId, String question, String option1, String option4, String option3, String option2, String answer, String description) {
        this.quizDate = quizDate;
        this.questionId = questionId;
        this.quizId = quizId;
        this.question = question;
        this.option1 = option1;
        this.option4 = option4;
        this.option3 = option3;
        this.option2 = option2;
        this.answer = answer;
        this.description = description;
    }

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
