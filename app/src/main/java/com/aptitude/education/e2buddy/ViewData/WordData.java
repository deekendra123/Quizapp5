package com.aptitude.education.e2buddy.ViewData;

public class WordData {
    String date, meaning, word, word_usage;

    public WordData(String date, String meaning, String word, String word_usage) {
        this.date = date;
        this.meaning = meaning;
        this.word = word;
        this.word_usage = word_usage;
    }


    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_usage() {
        return word_usage;
    }

    public void setWord_usage(String word_usage) {
        this.word_usage = word_usage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
