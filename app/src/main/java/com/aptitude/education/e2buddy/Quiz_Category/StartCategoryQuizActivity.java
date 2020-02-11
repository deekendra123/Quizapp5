package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

public class StartCategoryQuizActivity extends AppCompatActivity {


    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    Button start;
    String userid, parent_category, child_category;
    int quiz_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_category_quiz);

        tv1 = findViewById(R.id.textView6);
        tv2 = findViewById(R.id.textView5);
        tv3 = findViewById(R.id.textView7);
        tv4 = findViewById(R.id.textView8);
        tv5 = findViewById(R.id.textView9);
        tv6 = findViewById(R.id.textView10);

        tv7 = findViewById(R.id.textView11);

        start = findViewById(R.id.startquiz);

        parent_category = getIntent().getStringExtra("parent_category");
        child_category  = getIntent().getStringExtra("child_category");
        quiz_id  = getIntent().getIntExtra("quiz_id",0);

        Log.e("data ", parent_category+ child_category+quiz_id+userid);

        tv2.setText("1) Number of Questions : 10");

        tv3.setText("2) Attempt all the Questions");
        tv4.setText("3) Earn Coins for every Correct Answers");
        tv5.setText("4) No Negative Marking for Wrong Answer");
        tv6.setText("5) Use Coins to unlock Premium features");

        tv7.setText("Time Allotted : 15 sec/question");


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartCategoryQuizActivity.this, QuizCategoryQuestionActivity.class);
                intent.putExtra("parent_category",parent_category);
                intent.putExtra("child_category", child_category);
                intent.putExtra("quiz_id", quiz_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

    }
}
