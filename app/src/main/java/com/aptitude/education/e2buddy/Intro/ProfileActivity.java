package com.aptitude.education.e2buddy.Intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

     DatabaseReference databaseReference;
    FirebaseAuth auth;

     String email, username, eid,token_id;

     ProgressDialog progressDialog;
     EditText name;
     Spinner student_class;
    Button btlogin;
    String stud_class, student_name;
    List<String> list;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         name = findViewById(R.id.etname);
         btlogin = findViewById(R.id.btlogin);

        student_class = findViewById(R.id.spinner);


        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");
        auth = FirebaseAuth.getInstance();


        list = new ArrayList<>();
        list.add("Select Class");
        list.add("Class 1");
        list.add("Class 2");
        list.add("Class 3");
        list.add("Class 4");
        list.add("Class 5");
        list.add("Class 6");
        list.add("Class 7");
        list.add("Class 8");
        list.add("Class 9");
        list.add("Class 10");
        list.add("Class 11");
        list.add("Class 12");

        final FirebaseUser user = auth.getCurrentUser();
        username = user.getDisplayName();
        email = user.getEmail();
        eid = user.getUid();

        token_id = FirebaseInstanceId.getInstance().getToken();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        // adapter.setDropDownViewResource(R.layout.custom_textview_for_spinner);

        student_class.setAdapter(adapter);

        student_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                stud_class = adapterView.getItemAtPosition(i).toString();

                //  Toast.makeText(getApplicationContext(),"dk "+ item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean isInitialized = sharedPref.getBoolean("INIT_STATE", false);


        if (!isInitialized) {
            btlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    student_name = name.getText().toString();

                    if (student_name.isEmpty() && stud_class.equals("Select Class")){
                        Toast.makeText(getApplicationContext(),"Please fill the information", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        String id = databaseReference.push().getKey();
                        Data data = new Data(username, email, token_id, student_name);
                        databaseReference.child(eid).setValue(data);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("INIT_STATE", true);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                        startActivity(intent);

                        finish();
                    }
                }
            });

        }
        else {
            Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
