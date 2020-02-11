package com.aptitude.education.e2buddy.ViewData;

public class InternshipData {
    String card_id, logo, name, imageUrl;
    int remainingSlots, totalSlots;

    public InternshipData(String card_id, String logo, String name, String imageUrl, int remainingSlots, int totalSlots) {
        this.card_id = card_id;
        this.logo = logo;
        this.name = name;
        this.imageUrl = imageUrl;
        this.remainingSlots = remainingSlots;
        this.totalSlots = totalSlots;
    }


    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRemainingSlots() {
        return remainingSlots;
    }

    public void setRemainingSlots(int remainingSlots) {
        this.remainingSlots = remainingSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
