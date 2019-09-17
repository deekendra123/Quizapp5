package com.aptitude.education.e2buddy.ViewData;

/**
 * Created by Matrix on 21-12-2018.
 */

public class Data {


    String name;
    String email;
    String token_id;
    String student_name;



    public Data(String name, String email, String token_id, String student_name) {
        this.name = name;
        this.email = email;
        this.token_id = token_id;
        this.student_name = student_name;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
