package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 31-12-2018.
 */

public class HistoryView {
    String id;
    String latest_quiz_id;
    String quizname;
    String date;
    String coins;

    public HistoryView(String id, String quizname, String date, String coins) {
        this.id = id;
        this.quizname = quizname;
        this.date = date;
        this.coins = coins;
    }

    public HistoryView(String id, String latest_quiz_id, String quizname, String date, String coins) {
        this.id = id;
        this.latest_quiz_id = latest_quiz_id;
        this.quizname = quizname;
        this.date = date;
        this.coins = coins;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getLatest_quiz_id() {
        return latest_quiz_id;
    }

    public void setLatest_quiz_id(String latest_quiz_id) {
        this.latest_quiz_id = latest_quiz_id;
    }
}
