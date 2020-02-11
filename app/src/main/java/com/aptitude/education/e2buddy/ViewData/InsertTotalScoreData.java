package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 31-01-2019.
 */

public class InsertTotalScoreData {
    int  total_score;
    int final_score;
    int tournament_point_spent;
    int daily_quiz_total_score;
    int tournament_point_earn;


    public InsertTotalScoreData(int total_score) {
        this.total_score = total_score;
    }

    public InsertTotalScoreData(int final_score, int tournament_point_spent) {
        this.final_score = final_score;
        this.tournament_point_spent = tournament_point_spent;
    }

    public InsertTotalScoreData(int final_score, int tournament_point_spent, int daily_quiz_total_score, int tournament_point_earn) {
        this.final_score = final_score;
        this.tournament_point_spent = tournament_point_spent;
        this.daily_quiz_total_score = daily_quiz_total_score;
        this.tournament_point_earn = tournament_point_earn;
    }


    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public int getFinal_score() {
        return final_score;
    }

    public void setFinal_score(int final_score) {
        this.final_score = final_score;
    }

    public int getTournament_point_spent() {
        return tournament_point_spent;
    }

    public void setTournament_point_spent(int tournament_point_spent) {
        this.tournament_point_spent = tournament_point_spent;
    }

    public int getDaily_quiz_total_score() {
        return daily_quiz_total_score;
    }

    public void setDaily_quiz_total_score(int daily_quiz_total_score) {
        this.daily_quiz_total_score = daily_quiz_total_score;
    }
}
