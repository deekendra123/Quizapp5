package com.aptitude.education.e2buddy.Pushdatatofirebase;

import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CopyDbActivity extends AppCompatActivity {

    Cursor c = null;
    private  QuizDatabaseModel quizDatabaseModel;
    private ArrayList<QuizDatabaseModel1> quizDatabaseModelList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Button button;
    ListView listView;
    int max;
    private String quizId, quizDate, quizCategory ,questionId, question, option1,option4,option3,option2, answer, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_db);
        button = findViewById(R.id.button02);
        quizDatabaseModelList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseHelper myDbHelper = new DatabaseHelper(CopyDbActivity.this);


        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        c = myDbHelper.query("quiz1", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                quizDate =  c.getString(0);
                quizCategory = c.getString(1);
                questionId = c.getString(2);
                quizId = c.getString(3);
                question = c.getString(4);
                option1 = c.getString(5);
                option2 = c.getString(6);
                option3 = c.getString(7);
                option4 = c.getString(8);
                answer = c.getString(9);
                description = c.getString(10);

          /*      Toast.makeText(CopyDbActivity.this,
                        "date : " + c.getString(0)+ "\n"+
                                "questin_id : " + c.getString(1) + "\n" +
                                "quiz_id : " + c.getString(2) + "\n" +
                                "question: " + c.getString(3) + "\n" +
                                "option1 : "+ c.getString(4) + "\n" +
                                "option2 : " + c.getString(5)+ "\n"+
                                "option3 : "+ c.getString(6)+
                                "option4 : "+ c.getString(7)+
                                "answer :" + c.getString(8)+
                                "description : "+ c.getString(9),
                        Toast.LENGTH_SHORT).show();*/

                QuizDatabaseModel1 quizDatabaseModel =new QuizDatabaseModel1();

                quizDatabaseModel.setQuizDate(quizDate);
                quizDatabaseModel.setQuizCategory(quizCategory);
                quizDatabaseModel.setQuestionId(questionId);
                quizDatabaseModel.setQuizId(quizId);
                quizDatabaseModel.setQuestion(question);
                quizDatabaseModel.setOption1(option1);
                quizDatabaseModel.setOption2(option2);
                quizDatabaseModel.setOption3(option3);
                quizDatabaseModel.setOption4(option4);
                quizDatabaseModel.setAnswer(answer);
                quizDatabaseModel.setDescription(description);

                quizDatabaseModel = new QuizDatabaseModel1(quizDate,quizCategory, questionId, quizId, question, option1, option2, option3
                        , option4, answer, description);
                quizDatabaseModelList.add(quizDatabaseModel);
            } while (c.moveToNext());
        }

        ((Button) findViewById(R.id.button01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushToFirebase();


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDates("2019-07-23", "2019-10-31");
            }
        });
    }

    private void pushToFirebase(){

        int counter  = 1;
        if (!quizDatabaseModelList.isEmpty() && quizDatabaseModelList.size() > 0 ){
            int size = quizDatabaseModelList.size();
            for(int i =0; i<size; i++){
                counter++;
                if(counter > size)
                {
                    counter = 1;
                }
                databaseReference = firebaseDatabase.getReference();

               /* databaseReference = databaseReference.child("quiz_question");

                databaseReference =   databaseReference.child(quizDatabaseModelList.get(i).getQuizDate())
                        .child(quizDatabaseModelList.get(i).getQuizId()).child(""+counter);
*/
                databaseReference = databaseReference.child("questions");

                databaseReference =   databaseReference
                        .child(String.valueOf(counter));

                databaseReference.child("Question").setValue(quizDatabaseModelList.get(i).getQuestion())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

                databaseReference.child("Option1").setValue(quizDatabaseModelList.get(i).getOption1())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                databaseReference.child("Option2").setValue(quizDatabaseModelList.get(i).getOption2())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                databaseReference.child("Option3").setValue(quizDatabaseModelList.get(i).getOption3())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                databaseReference.child("Option4").setValue(quizDatabaseModelList.get(i).getOption4())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                databaseReference.child("Answer").setValue(quizDatabaseModelList.get(i).getAnswer())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                databaseReference.child("Description").setValue(quizDatabaseModelList.get(i).getDescription())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }

                        });
            }
        }

    }

    private List<Date> getDates(String dateString1, String dateString2)
    {

        DatabaseReference reference ;

        reference = FirebaseDatabase.getInstance().getReference("daily_Question");

        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");


        DateFormat outputFormat = new SimpleDateFormat("dd-MM", Locale.US);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);


        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            Date date = cal1.getTime();
            cal1.add(Calendar.DATE, 1);
            Toast.makeText(CopyDbActivity.this, ""+outputFormat.format(date), Toast.LENGTH_SHORT).show();

            Random random = new Random();
            for (int i = 0; i<10; i++){

                int max = random.nextInt(415-1+1)+1;
                reference.child(outputFormat.format(date)).child(String.valueOf(max)).setValue(i);

            }
        }
        return dates;
    }

}
