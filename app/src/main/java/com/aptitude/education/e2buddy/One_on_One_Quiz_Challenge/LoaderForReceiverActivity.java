package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoaderForReceiverActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String sender_id,sender_name,notification_id;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_for_receiver);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        preferences = getSharedPreferences("sender_info",MODE_PRIVATE);

        sender_id = preferences.getString("s_id","");
        sender_name = preferences.getString("sender_name","");
        notification_id = preferences.getString("not_id","");

        //    Toast.makeText(getApplicationContext(),""+notification_id+ "\n"+sender_id+"\n"+sender_name,Toast.LENGTH_SHORT).show();




        dialog = new ProgressDialog(LoaderForReceiverActivity.this,R.style.full_screen_dialog){
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


                Intent intent = new Intent(getApplicationContext(), QuestionForUser2Activity.class);
                startActivity(intent);


            }
        }, delayInMillis);


    }

}
