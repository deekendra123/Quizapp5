package com.aptitude.education.e2buddy.ViewData;

public class BookmarkData {
    String question_id;
    boolean bookmark;
    int position;
    String answer;

    public BookmarkData(String question_id, boolean bookmark, int position) {
        this.question_id = question_id;
        this.bookmark = bookmark;
        this.position = position;
    }

    public BookmarkData(boolean bookmark, int position, String answer) {
        this.bookmark = bookmark;
        this.position = position;
        this.answer = answer;
    }

    public BookmarkData(String question_id, boolean bookmark, int position, String answer) {
        this.question_id = question_id;
        this.bookmark = bookmark;
        this.position = position;
        this.answer = answer;
    }


    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
