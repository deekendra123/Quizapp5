package com.aptitude.education.e2buddy.ViewData;

public class TournamentRegisterStudentData {
    int points_spent;
    boolean play_status;


    public TournamentRegisterStudentData(int points_spent, boolean play_status) {
        this.points_spent = points_spent;
        this.play_status = play_status;
    }

    public int getPoints_spent() {
        return points_spent;
    }

    public void setPoints_spent(int points_spent) {
        this.points_spent = points_spent;
    }

    public boolean isPlay_status() {
        return play_status;
    }

    public void setPlay_status(boolean play_status) {
        this.play_status = play_status;
    }
}
