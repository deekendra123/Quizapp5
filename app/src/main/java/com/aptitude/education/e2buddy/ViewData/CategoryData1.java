package com.aptitude.education.e2buddy.ViewData;

public class CategoryData1 {
    String parent_category;
    String child_category;
    int imageUrl;
    int quiz_id;




    public CategoryData1(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryData1(String parent_category, String child_category) {
        this.parent_category = parent_category;
        this.child_category = child_category;
    }

    public CategoryData1(String parent_category, String child_category, int imageUrl, int quiz_id) {
        this.parent_category = parent_category;
        this.child_category = child_category;
        this.imageUrl = imageUrl;
        this.quiz_id = quiz_id;
    }

    public CategoryData1(String parent_category, String child_category, int quiz_id) {
        this.parent_category = parent_category;
        this.child_category = child_category;
        this.quiz_id = quiz_id;
    }

    public String getParent_category() {
        return parent_category;
    }

    public void setParent_category(String parent_category) {
        this.parent_category = parent_category;
    }

    public String getChild_category() {
        return child_category;
    }

    public void setChild_category(String child_category) {
        this.child_category = child_category;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }
}
