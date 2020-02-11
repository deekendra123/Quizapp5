package com.aptitude.education.e2buddy.ViewData;

import androidx.annotation.NonNull;

public class GroupResultData implements Comparable{
    String player_name;
    int score;
    int question_answered;
    String player_id;

    public GroupResultData(String player_name, int score, int question_answered, String player_id) {
        this.player_name = player_name;
        this.score = score;
        this.question_answered = question_answered;
        this.player_id = player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestion_answered() {
        return question_answered;
    }

    public void setQuestion_answered(int question_answered) {
        this.question_answered = question_answered;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compare = getScore();
        return  this.score-compare;
    }
}
