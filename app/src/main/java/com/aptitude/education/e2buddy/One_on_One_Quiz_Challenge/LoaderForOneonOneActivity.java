package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoaderForOneonOneActivity extends AppCompatActivity {

    SharedPreferences preferences,sharedPreferences;
    String notification_id, receiver_id, receiver_name;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_for_oneon_one);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        preferences = getSharedPreferences("receiver_info",MODE_PRIVATE);

        notification_id = preferences.getString("notification_ids","");
        receiver_id = preferences.getString("receiver_id","");
        receiver_name = preferences.getString("receiver_name","");


        //    Toast.makeText(getApplicationContext(),""+notification_id+ "\n"+receiver_id+"\n"+receiver_name,Toast.LENGTH_SHORT).show();

        dialog = new ProgressDialog(LoaderForOneonOneActivity.this,R.style.full_screen_dialog){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.full_progress_dialog);
                getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.FILL_PARENT);
            }
        };

        dialog.setTitle("Please Wait...");
        dialog.setMessage("Initiating the game");
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 7500;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), QuestionForQuizChallengeActivity.class);
                startActivity(intent);

            }
        }, delayInMillis);

    }

}
