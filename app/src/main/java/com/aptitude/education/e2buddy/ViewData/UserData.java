package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 21-12-2018.
 */

public class UserData {

    String email;
    String token_id;
    String student_name;
    String phone_no;
    String image_Url;


    public UserData(String email, String token_id, String student_name, String phone_no) {
        this.email = email;
        this.token_id = token_id;
        this.student_name = student_name;
        this.phone_no = phone_no;

    }

    public UserData(String email, String token_id, String student_name) {
        this.email = email;
        this.token_id = token_id;
        this.student_name = student_name;

    }

    public UserData(String email, String token_id, String student_name, String phone_no, String image_Url) {
        this.email = email;
        this.token_id = token_id;
        this.student_name = student_name;
        this.phone_no = phone_no;
        this.image_Url = image_Url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }
}
