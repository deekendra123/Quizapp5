package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 13-02-2019.
 */

public class TimeData {

    long timelefts;

    int correct_answer;
    String timeleft;

    public TimeData(long timelefts) {
        this.timelefts = timelefts;
    }

    public TimeData(int correct_answer, String timeleft) {
        this.correct_answer = correct_answer;
        this.timeleft = timeleft;
    }

    public long getTimelefts() {
        return timelefts;
    }

    public void setTimelefts(long timelefts) {
        this.timelefts = timelefts;
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
