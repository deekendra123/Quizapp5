package com.aptitude.education.e2buddy.Pushdatatofirebase;

import android.database.Cursor;
import android.database.SQLException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    Cursor c1 = null;
    Cursor c2 = null;
    private ArrayList<QuizDatabaseModel1> quizDatabaseModelList;
    private ArrayList<QuizDatabaseModel1> wordDatabaseModelList;
    private ArrayList<CategoryDatabaseModel> categoryDatabaseModelsList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Button button,button1, button2, button3;
    private String quizId, quizDate, quizCategory ,questionId, question, option1,option4,option3,option2, answer, description;
    private String wordDate, wordId, word, meaning, word_usage;
    String parent_category, date, child_category, question_id, quiz_id, cat_question, cat_option1, cat_option2, cat_option3, cat_option4, cat_answer, cat_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_db);
        button = findViewById(R.id.button02);
        button1 = findViewById(R.id.button03);
        button2 = findViewById(R.id.button04);
        button3 = findViewById(R.id.button05);
        quizDatabaseModelList = new ArrayList<>();
        wordDatabaseModelList = new ArrayList<>();
        categoryDatabaseModelsList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
      //  DatabaseHelper myDbHelper = new DatabaseHelper(CopyDbActivity.this);
        DatabaseHelperForWord helper = new DatabaseHelperForWord(CopyDbActivity.this);
     //   DatabaseHelperForCategoryQuestion helper1 = new DatabaseHelperForCategoryQuestion(CopyDbActivity.this);

        try {
           // myDbHelper.createDataBase();
            helper.createDataBase();
            //helper1.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
           // myDbHelper.openDataBase();
           helper.openDataBase();
          //  helper1.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
     //   c = myDbHelper.query("quiz2", null, null, null, null, null, null);
        c1 = helper.query("wordofthemonth", null, null, null, null, null, null);
        //c2 = helper1.query("category_quiz", null, null, null, null, null, null);

       /* if (c.moveToFirst()) {
            do {

                question = c.getString(5);
                option1 = c.getString(6);
                option2 = c.getString(7);
                option3 = c.getString(8);
                option4 = c.getString(9);
                answer = c.getString(10);
                description = c.getString(11);
                QuizDatabaseModel1 quizDatabaseModel =new QuizDatabaseModel1();

                quizDatabaseModel.setQuestion(question);
                quizDatabaseModel.setOption1(option1);
                quizDatabaseModel.setOption2(option2);
                quizDatabaseModel.setOption3(option3);
                quizDatabaseModel.setOption4(option4);
                quizDatabaseModel.setAnswer(answer);
                quizDatabaseModel.setDescription(description);
                quizDatabaseModel = new QuizDatabaseModel1(question, option1, option2, option3
                        , option4, answer, description);
                quizDatabaseModelList.add(quizDatabaseModel);
            } while (c.moveToNext());
        }
*/
        if (c1.moveToFirst()) {
            do {
                wordDate =  c1.getString(0);
                wordId = c1.getString(1);
                word = c1.getString(2);
                meaning = c1.getString(3);
                word_usage = c1.getString(4);

                QuizDatabaseModel1 quizDatabaseModel =new QuizDatabaseModel1();
                quizDatabaseModel.setWordDate(wordDate);
                quizDatabaseModel.setWordId(wordId);
                quizDatabaseModel.setWord(word);
                quizDatabaseModel.setMeaning(meaning);
                quizDatabaseModel.setWord_usage(word_usage);
                quizDatabaseModel = new QuizDatabaseModel1(wordDate, wordId, word, meaning, word_usage);
                wordDatabaseModelList.add(quizDatabaseModel);
            } while (c1.moveToNext());
        }
/*
        if (c2.moveToFirst()) {
            do {
                parent_category =  c2.getString(0);
                date = c2.getString(1);
                child_category = c2.getString(2);
                question_id = c2.getString(3);
                quiz_id = c2.getString(4);
                cat_question = c2.getString(5);
                cat_option1 = c2.getString(6);
                cat_option2 = c2.getString(7);
                cat_option3 = c2.getString(8);
                cat_option4 = c2.getString(9);
                cat_answer = c2.getString(10);
                cat_description = c2.getString(11);
                CategoryDatabaseModel categoryDatabaseModel =new CategoryDatabaseModel();

                categoryDatabaseModel.setParent_category(parent_category);
                categoryDatabaseModel.setChild_category(child_category);
                categoryDatabaseModel.setQuestion_id(question_id);
                categoryDatabaseModel.setQuestion(cat_question);
                categoryDatabaseModel.setQuestion(question);
                categoryDatabaseModel.setOption1(cat_option1);
                categoryDatabaseModel.setOption2(cat_option2);
                categoryDatabaseModel.setOption3(cat_option3);
                categoryDatabaseModel.setOption4(cat_option4);
                categoryDatabaseModel.setAnswer(cat_answer);
                categoryDatabaseModel.setDescription(cat_description);

                //Toast.makeText(getApplicationContext(), ""+ child_category,Toast.LENGTH_SHORT).show();

                Log.e("deeke ", parent_category + "        "+ child_category);

                categoryDatabaseModel = new CategoryDatabaseModel(parent_category, date ,child_category, question_id, quiz_id, cat_question, cat_option1, cat_option2, cat_option3, cat_option4
                        , cat_answer, cat_description);
                categoryDatabaseModelsList.add(categoryDatabaseModel);
            } while (c2.moveToNext());
        }
*/

        findViewById(R.id.button01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToFirebase();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDates("2019-12-26", "2020-03-31");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTournamentDates("2020-04-01", "2020-05-31");
            }
        });
         button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushWordoftheMonth();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pushCategoryQuestion();
            }
        });
    }

    private void pushCategoryQuestion(){

        if (!categoryDatabaseModelsList.isEmpty() && categoryDatabaseModelsList.size() > 0 ){
            int size = categoryDatabaseModelsList.size();
            for(int i =0; i<size; i++){

                    databaseReference = firebaseDatabase.getReference();
                    databaseReference =   databaseReference.child("category_questions")
                            .child(String.valueOf(categoryDatabaseModelsList.get(i).getParent_category()))
                            .child(categoryDatabaseModelsList.get(i).getChild_category())
                            .child(categoryDatabaseModelsList.get(i).getQuiz_id())
                            .child(categoryDatabaseModelsList.get(i).getQuestion_id());


                    databaseReference.child("Question").setValue(categoryDatabaseModelsList.get(i).getQuestion())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                    databaseReference.child("Option1").setValue(categoryDatabaseModelsList.get(i).getOption1())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                    databaseReference.child("Option2").setValue(categoryDatabaseModelsList.get(i).getOption2())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                    databaseReference.child("Option3").setValue(categoryDatabaseModelsList.get(i).getOption3())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                    databaseReference.child("Option4").setValue(categoryDatabaseModelsList.get(i).getOption4())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                    databaseReference.child("Answer").setValue(categoryDatabaseModelsList.get(i).getAnswer())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                    databaseReference.child("Description").setValue(categoryDatabaseModelsList.get(i).getDescription())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }

                            });
                }
        }

    }
    private void pushToFirebase(){

        int counter  = 1;
        if (!quizDatabaseModelList.isEmpty() && quizDatabaseModelList.size() > 0 ){
            int size = quizDatabaseModelList.size();
            for(int i =0; i<size; i++){
                counter++;
                Log.e("goku ", String.valueOf(counter));
                databaseReference = firebaseDatabase.getReference();

                databaseReference =   databaseReference.child("questions")
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



    private void pushWordoftheMonth(){

        if (!wordDatabaseModelList.isEmpty() && wordDatabaseModelList.size() > 0 ){

            int size = wordDatabaseModelList.size();
            for(int i =0; i<size; i++){
                databaseReference = firebaseDatabase.getReference();

                databaseReference =   databaseReference.child("word_of_the_month")
                        .child(wordDatabaseModelList.get(i).getWordDate()).child(wordDatabaseModelList.get(i).getWordId());

                databaseReference.child("word").setValue(wordDatabaseModelList.get(i).getWord())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                databaseReference.child("meaning").setValue(wordDatabaseModelList.get(i).getMeaning())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                databaseReference.child("word_usage").setValue(wordDatabaseModelList.get(i).getWord_usage())
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

        reference = FirebaseDatabase.getInstance().getReference();

        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");


        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

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

            Random random = new Random();
            for (int i = 0; i<10; i++){

                int max = random.nextInt(779-1+1)+1;
                reference.child("daily_Question").child(outputFormat.format(date)).child(String.valueOf(max)).setValue(i);
                Log.e("deeke ", String.valueOf(date));
            }

        }
        return dates;
    }


    private List<Date> getTournamentDates(String dateString1, String dateString2)
    {

        DatabaseReference reference ;

        reference = FirebaseDatabase.getInstance().getReference("tournament_question_ids");

        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

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

            if (cal1.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY || cal1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                Date date = cal1.getTime();

                    Random random = new Random();
             for (int i = 0; i<10; i++){

                int max = random.nextInt(415-1+1)+1;
                reference.child(outputFormat.format(date)).child(String.valueOf(max)).setValue(i);
                Log.e("deeke ", String.valueOf(date));
            }

            }
            cal1.add(Calendar.DATE, 1);

        }
        return dates;
    }

}

