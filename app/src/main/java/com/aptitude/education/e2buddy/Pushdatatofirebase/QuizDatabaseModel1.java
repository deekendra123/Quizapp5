package com.aptitude.education.e2buddy.Pushdatatofirebase;

/**
 * Created by Matrix on 18-01-2019.
 */

public class QuizDatabaseModel1 {
    public QuizDatabaseModel1() {
    }

    String quizDate,quizCategory,questionId, quizId , question, option1,option2,option3,option4, answer, description;

    String wordDate, wordId, word, meaning, word_usage;


    public QuizDatabaseModel1(String quizDate, String quizCategory, String questionId, String quizId, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        this.quizDate = quizDate;
        this.quizCategory = quizCategory;
        this.questionId = questionId;
        this.quizId = quizId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
    }

    public QuizDatabaseModel1(String wordDate, String wordId, String word, String meaning, String word_usage) {
        this.wordDate = wordDate;
        this.wordId = wordId;
        this.word = word;
        this.meaning = meaning;
        this.word_usage = word_usage;
    }

    public QuizDatabaseModel1(String question, String option1, String option2, String option3, String option4, String answer, String description) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
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

    public String getQuizCategory() {
        return quizCategory;
    }

    public void setQuizCategory(String quizCategory) {
        this.quizCategory = quizCategory;
    }

    public String getWordDate() {
        return wordDate;
    }

    public void setWordDate(String wordDate) {
        this.wordDate = wordDate;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord_usage() {
        return word_usage;
    }

    public void setWord_usage(String word_usage) {
        this.word_usage = word_usage;
    }
}
