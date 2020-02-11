package com.aptitude.education.e2buddy.Intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "E2Buddy";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_PLAYER_ID = "student_id";

    public static final String KEY_PLAYER_NAME = "student_name";

    public static final String KEY_PLAYER_EMAIL = "student_email";

    public static final String KEY_PLAYER_IMAGE_URL = "image_url";

    public static final String KEY_DAILY_QUIZ_ID = "daily_quiz_id";

    public static final String KEY_TOURNAMENT_QUIZ_ID = "tournament_quiz_id";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createStudentData(String student_id, String student_name, String student_email, String student_image_url, String daily_quiz_id, String tournament_quiz_id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_PLAYER_ID, student_id);
        editor.putString(KEY_PLAYER_NAME, student_name);
        editor.putString(KEY_PLAYER_EMAIL, student_email);
        editor.putString(KEY_PLAYER_IMAGE_URL, student_image_url);
        editor.putString(KEY_DAILY_QUIZ_ID, daily_quiz_id);
        editor.putString(KEY_TOURNAMENT_QUIZ_ID, tournament_quiz_id);


        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, IntroActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    public HashMap<String, String> getData(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_PLAYER_ID, pref.getString(KEY_PLAYER_ID, null));
        user.put(KEY_PLAYER_NAME, pref.getString(KEY_PLAYER_NAME, null));
        user.put(KEY_PLAYER_EMAIL, pref.getString(KEY_PLAYER_EMAIL, null));
        user.put(KEY_PLAYER_IMAGE_URL, pref.getString(KEY_PLAYER_IMAGE_URL, null));
        user.put(KEY_DAILY_QUIZ_ID, pref.getString(KEY_DAILY_QUIZ_ID, null));
        user.put(KEY_TOURNAMENT_QUIZ_ID, pref.getString(KEY_TOURNAMENT_QUIZ_ID, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, IntroActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}

