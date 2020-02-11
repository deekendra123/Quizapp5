package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CategoryData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class CategoryQuizAdapter extends RecyclerView.Adapter<CategoryQuizAdapter.SubCategoryHolder> {

    Context mCtx;
    List<CategoryData> list;
    DatabaseReference databaseReference;
    AlertDialog alertDialog;
    SessionManager sessionManager;


    public CategoryQuizAdapter(Context mCtx, List<CategoryData> list, AlertDialog alertDialog) {
        this.mCtx = mCtx;
        this.list = list;
        this.alertDialog = alertDialog;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_list, parent, false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryHolder holder, int position) {


        final CategoryData categoryData = list.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sessionManager = new SessionManager(mCtx);

        HashMap<String, String> user = sessionManager.getData();

        String playerId = user.get(SessionManager.KEY_PLAYER_ID);


        int quizid = position+1;
        holder.tvquiz.setText("Quiz "+quizid);


        databaseReference.child("category_user_credit").child(playerId).child(categoryData.getParent_category()).child(categoryData.getChild_category()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(String.valueOf(categoryData.getQuiz_id()))){
                    holder.imgSubCategroy.setVisibility(View.VISIBLE);
                 }
               }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.gc();

                Intent intent = new Intent(mCtx, StartCategoryQuizActivity.class);
                intent.putExtra("parent_category",categoryData.getParent_category());
                intent.putExtra("child_category", categoryData.getChild_category());
                intent.putExtra("quiz_id", categoryData.getQuiz_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mCtx.startActivity(intent);
                alertDialog.dismiss();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {

        TextView tvquiz;
        ImageView imgSubCategroy;
        public SubCategoryHolder(View itemView) {
            super(itemView);
            tvquiz = itemView.findViewById(R.id.tvquiz);
            imgSubCategroy = itemView.findViewById(R.id.img);

        }

    }
}
