package com.aptitude.education.e2buddy.Intro;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
//import com.hbb20.CountryCodePicker;

public class PhoneVerificationActivity extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText name, number, email;
    TextView textView,textView1;
    ImageView request;
    Animation frombottem,fromtop;
    TextView tv;
    FirebaseAuth firebaseAuth;
    String  token_id, emailid, student_name;
    DatabaseReference databaseReference;
    ImageView icon;
    CountryCodePicker ccp;

    //  CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        name = findViewById(R.id.editText11);
        number = findViewById(R.id.editText12);
        request = findViewById(R.id.button8);
        email = findViewById(R.id.editText13);
        textView = findViewById(R.id.t1);
        textView1 = findViewById(R.id.t2);
        tv = findViewById(R.id.t4);

        ccp = findViewById(R.id.ccp);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

      /*  name.setHint(name.getHint()+" "+getString(R.string.asteriskred));
        number.setHint(number.getHint()+" "+getString(R.string.asteriskred));
*///        email.setHint(email.getHint()+" "+getString(R.string.asteriskred));

        icon = findViewById(R.id.logo);

        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        icon.setAnimation(fromtop);

        name.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



        /*ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(getApplicationContext(), "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
                    textView.startAnimation(animation);
                    sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

     databaseReference = FirebaseDatabase.getInstance().getReference("user_info");

        token_id = FirebaseInstanceId.getInstance().getToken();


        firebaseAuth = FirebaseAuth.getInstance();



        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ccp.registerPhoneNumberTextView(number);

                String phonenumber = number.getText().toString().trim();
                String username = name.getText().toString().trim();
                String  emailid = email.getText().toString().trim();
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(getApplicationContext());

                String fullnumber = ccp.getFullNumberWithPlus();

                try {
                    Phonenumber.PhoneNumber usNumberProto = phoneUtil.parse(fullnumber, "");            //with default country
                    boolean isValid = phoneUtil.isValidNumber(usNumberProto);                  //returns true
                    String usNumber = phoneUtil.format(usNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164); //+12025550100


                if (phonenumber.isEmpty() || number.length() < 10||isValid==false) {
                    number.setError("Valid number is required");
                    number.requestFocus();
                    return;
                }
                else if (username.isEmpty()) {
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                else {

                    student_name = name.getText().toString();

                    Intent intent = new Intent(PhoneVerificationActivity.this, OTPVerificationActivity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("student_name", student_name);
                    intent.putExtra("email_id", emailid);
                    startActivity(intent);

                }
                } catch (NumberParseException e) {
                    Log.e("deeke","NumberParseException was thrown: " + e.toString());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeNevActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }


}
