package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 21-02-2019.
 */

public class OnlineStatusData {
    String user_name;
    String user_email;
    boolean online_status;
    String user_id;
    String imageUrl;


    public OnlineStatusData(String user_name, boolean online_status, String imageUrl) {
        this.user_name = user_name;
        this.online_status = online_status;
        this.imageUrl = imageUrl;
    }

    public OnlineStatusData(String user_name, boolean online_status) {
        this.user_name = user_name;
        this.online_status = online_status;
    }

    public OnlineStatusData(String user_name, boolean online_status, String user_id, String imageUrl) {
        this.user_name = user_name;
        this.online_status = online_status;
        this.user_id = user_id;
        this.imageUrl = imageUrl;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public boolean isOnline_status() {
        return online_status;
    }

    public void setOnline_status(boolean online_status) {
        this.online_status = online_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
