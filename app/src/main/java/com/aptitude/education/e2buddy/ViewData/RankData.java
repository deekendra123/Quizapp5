package com.aptitude.education.e2buddy.ViewData;

public class RankData {
    String userid;
    int rank;


    public RankData(String userid, int rank) {
        this.userid = userid;
        this.rank = rank;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
