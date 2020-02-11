package com.aptitude.education.e2buddy.Pushdatatofirebase;

public class CategoryDatabaseModel {

    String parent_category,date, child_category, question_id, quiz_id, question, option1, option2, option3, option4, answer, description;

    public CategoryDatabaseModel(){

    }

    public CategoryDatabaseModel(String parent_category, String date, String child_category, String question_id, String quiz_id, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        this.parent_category = parent_category;
        this.date = date;
        this.child_category = child_category;
        this.question_id = question_id;
        this.quiz_id = quiz_id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
    }


    public String getParent_category() {
        return parent_category;
    }

    public void setParent_category(String parent_category) {
        this.parent_category = parent_category;
    }

    public String getChild_category() {
        return child_category;
    }

    public void setChild_category(String child_category) {
        this.child_category = child_category;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
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

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }
}
