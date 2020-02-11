package com.aptitude.education.e2buddy.Quiz_Category;

import android.app.ProgressDialog;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AnswerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class CategoryResultActivity extends AppCompatActivity {

    String parent_category, child_category, userId;
    int quiz_id,score;
    Transformation transformation;
    ImageView img1, img2, img3, img4, userIcon;
    TextView tv1,tv2,tvscore,tvanswer, tvCorrectAnswer;
    Button btviewans;
    LinearLayout linearLayout;
    DatabaseReference databaseReference;
    List<AnswerView> answerViewList;
    ValueEventListener valueEventListener;
    int corr_answer;
    ProgressDialog progressDialog;
    static CategoryResultActivity categoryResultActivity;

    public static CategoryResultActivity getInstance(){
        return   categoryResultActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_result);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tscore);
        tvscore = findViewById(R.id.tscore1);
        userIcon = findViewById(R.id.userimg);
        btviewans = findViewById(R.id.btview);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        linearLayout = findViewById(R.id.linear);
        tvCorrectAnswer = findViewById(R.id.tvcorrectans);
        tvanswer = findViewById(R.id.tvans);

        categoryResultActivity = this;

        progressDialog = new ProgressDialog(CategoryResultActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        parent_category = getIntent().getStringExtra("parent_category");
        child_category  = getIntent().getStringExtra("child_category");
        quiz_id  = getIntent().getIntExtra("quiz_id",0);
        userId = getIntent().getStringExtra("userid");

        Log.e("data ", parent_category+ child_category+quiz_id+userId);


        answerViewList = new ArrayList<>();
        getScore();
        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
        getPlayerImage();


        btviewans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment dialog = View_Category_Answer_Dialog.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("parent_category",parent_category);
                bundle.putString("child_category",child_category);
                bundle.putInt("quiz_id", quiz_id);
                bundle.putString("userid",userId);
                dialog.setArguments(bundle);
                dialog.show(fm,"tag");


            }
        });

    }

    private void getScore() {
    databaseReference.child("category_user_credit").child(userId).child(parent_category).child(child_category).child(String.valueOf(quiz_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {

                        score = Integer.parseInt(dataSnapshot.child("credit_points").getValue().toString());
                        corr_answer = Integer.parseInt(dataSnapshot.child("correct_answers").getValue().toString());

                        tvscore.setText("" + score);
                        tvCorrectAnswer.setText("" + corr_answer + "/10");
                        progressDialog.dismiss();
                        if (corr_answer <= 4) {
                            linearLayout.setVisibility(View.GONE);
                            img1.setVisibility(View.GONE);
                            img2.setVisibility(View.GONE);
                            img3.setVisibility(View.GONE);

                            ViewGroup.MarginLayoutParams marginParams1 = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                            marginParams1.setMargins(0, 40, 0, 0);

                            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
                            marginParams.setMargins(0, 50, 0, 35);


                            tv1.setTextSize(21f);
                            img4.setBackgroundResource(R.drawable.goforit);
                            tv1.setText("Better Luck Next Time");
                        } else {
                            img4.setBackgroundResource(R.drawable.ic_achievement);
                            tv1.setText("Congratulations!");

                    }
                }

                catch (Exception e){
                    e.printStackTrace();
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getPlayerImage(){

        valueEventListener = databaseReference.child("user_info").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
