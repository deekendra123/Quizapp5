package com.aptitude.education.e2buddy.ViewData;

public class AcceptedUsersData {
    String userid;
    String username;

    public AcceptedUsersData(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
