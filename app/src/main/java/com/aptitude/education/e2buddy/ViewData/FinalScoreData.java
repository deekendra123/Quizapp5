package com.aptitude.education.e2buddy.ViewData;

public class FinalScoreData {
    int daily_quiz_total_score;
    int tournament_point_spent;

    public FinalScoreData(int daily_quiz_total_score, int tournament_point_spent) {
        this.daily_quiz_total_score = daily_quiz_total_score;
        this.tournament_point_spent = tournament_point_spent;
    }

    public int getDaily_quiz_total_score() {
        return daily_quiz_total_score;
    }

    public void setDaily_quiz_total_score(int daily_quiz_total_score) {
        this.daily_quiz_total_score = daily_quiz_total_score;
    }

    public int getTournament_point_spent() {
        return tournament_point_spent;
    }

    public void setTournament_point_spent(int tournament_point_spent) {
        this.tournament_point_spent = tournament_point_spent;
    }
}
