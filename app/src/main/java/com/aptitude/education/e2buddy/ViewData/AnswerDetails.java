package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 13-02-2019.
 */

public class AnswerDetails {


    int correct_answer;
    String timeleft;


    public AnswerDetails(int correct_answer, String timeleft) {
        this.correct_answer = correct_answer;
        this.timeleft = timeleft;
    }


    public int getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(int correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }
}
