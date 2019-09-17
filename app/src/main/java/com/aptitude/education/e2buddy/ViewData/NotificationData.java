package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 25-02-2019.
 */

public class NotificationData {

    String sender_id;
    String sender_name;
    String hour;
    String minut;
    String second;

    public NotificationData(String sender_id, String sender_name) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
    }

    public NotificationData(String sender_id, String sender_name, String hour, String minut, String second) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.hour = hour;
        this.minut = minut;
        this.second = second;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinut() {
        return minut;
    }

    public void setMinut(String minut) {
        this.minut = minut;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }


  }
