package com.aptitude.education.e2buddy.Placement_Papers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.BookmarkData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookmarkQuestionActivity extends AppCompatActivity {

    RecyclerView recyclerViewBookmarks;
    DatabaseReference databaseReference;
    List<BookmarkData> list;
    String playerId, playerName;
    SessionManager sessionManager;
    BookmarksAdapter bookmarksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_question);

        ImageButton closeDialog = findViewById(R.id.fullscreen_dialog_close);
        recyclerViewBookmarks = findViewById(R.id.recyclerViewBookmarks);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookmarkQuestionActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewBookmarks.setLayoutManager(layoutManager);

        sessionManager = new SessionManager(BookmarkQuestionActivity.this);
        HashMap<String, String> user = sessionManager.getData();
        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);


        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bookmarksAdapter = new BookmarksAdapter(this, list);

        CheckInternet checkInternet = new CheckInternet(BookmarkQuestionActivity.this);
        checkInternet.checkConnection();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();

                onBackPressed();
            }
        });


        databaseReference.child("placemenPaper_user_answer").child(playerId).child("quantitativeAbility").child("quiz1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String question_id = dataSnapshot1.getKey();
                    boolean bookmark_status = (boolean) dataSnapshot1.child("bookmark").getValue();
                    int position = Integer.parseInt(dataSnapshot1.child("position").getValue().toString());
                    Log.e("bookmark_status", question_id + "  "+ bookmark_status);

                    list.add(new BookmarkData(question_id, bookmark_status, position));
                }

                recyclerViewBookmarks.setAdapter(bookmarksAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bookmarksAdapter.setOnItemClickListener(new BookmarksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                onBackPressed();

                BookmarkData leaderBoardData = list.get(position);
                SharedPreferences sp = getSharedPreferences("bookmark", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("questionid",leaderBoardData.getQuestion_id());
                editor.putInt("position", leaderBoardData.getPosition());

                editor.commit();
            }
        });


    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences("bookmark", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Log.e("clear", String.valueOf(editor.clear()));
        BookmarkQuestionActivity.super.onBackPressed();
    }
}
