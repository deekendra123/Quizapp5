package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 26-11-2018.
 */

public class QuestionView1 {


    private String questionid;
    private String questioname;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;

    private String answernull;




    public QuestionView1() {

    }

    public QuestionView1(String questionid, String questioname, String option1, String option2, String option3, String option4, String answer, String answernull) {
        this.questionid = questionid;
        this.questioname = questioname;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.answernull = answernull;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getQuestioname() {
        return questioname;
    }

    public void setQuestioname(String questioname) {
        this.questioname = questioname;
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

    public String getAnswernull() {
        return answernull;
    }

    public void setAnswernull(String answernull) {
        this.answernull = answernull;
    }
}
