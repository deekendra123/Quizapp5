package com.aptitude.education.e2buddy.Intro;

import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {
    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private PinEntryEditText editText;
    String  token_id,  emailid, student_name;
    String phonenumber;
    TextView tvno, resend_otp, timer;
    private static final String FORMAT = "%02d:%02d";
    TextView tv1, tv2, tv3;
    CountDownTimer countDownTimer;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.txt_pin_entry);
        tvno = findViewById(R.id.tvno);
        resend_otp = findViewById(R.id.otpresend);
        timer = findViewById(R.id.tvtimers);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
       // buttonSignIn = findViewById(R.id.buttonSignIn);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();


        user = FirebaseAuth.getInstance().getCurrentUser();


        phonenumber = getIntent().getStringExtra("phonenumber");

        student_name = getIntent().getStringExtra("student_name");
        emailid = getIntent().getStringExtra("email_id");

        tvno.setText("+91-"+phonenumber);

        resend_otp.setPaintFlags(resend_otp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        sendVerificationCode(phonenumber);
        timerforResendOtp();


                editText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                    @Override
                    public void onPinEntered(CharSequence str) {
                        final String code = editText.getText().toString().trim();

                        if ((code.isEmpty() || code.length() < 6)){

                            editText.setError("Enter code...");
                            editText.requestFocus();
                            return;
                        }
                        else {

                            verifyCode(code);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

              // verifyCode(code);


        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                timerforResendOtp();
             //   sendVerificationCode(phonenumber);
                resendOtp(phonenumber);
            }
        });


    }

    private void resendOtp(String phoneNumber) {


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallBack,          // OnVerificationStateChangedCallbacks
                        forceResendingToken);


        }
    private void verifyCode(String code){
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithCredential(credential);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            if (user != null) {
                                // User is signed in
                                Intent i = new Intent(OTPVerificationActivity.this, HomeNevActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            else {
                                addData();
                                Intent intent = new Intent(OTPVerificationActivity.this, InsertUserImageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();
                            }



                        } else {
                            Toast.makeText(OTPVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPVerificationActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };



    private void addData(){

        String eid;
        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");

        token_id = FirebaseInstanceId.getInstance().getToken();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        eid = user.getUid();

            UserData data = new UserData(emailid, token_id, student_name,phonenumber);
            databaseReference.child(eid).setValue(data);

    }

    private void timerforResendOtp(){
      countDownTimer =  new CountDownTimer(25000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext

                timer.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        ), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                timer.setText("00:00");
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
        startActivity(i);
        finish();
    }


}
