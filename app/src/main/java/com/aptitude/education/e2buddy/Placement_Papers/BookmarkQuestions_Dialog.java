package com.aptitude.education.e2buddy.Placement_Papers;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Menu.Email;
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

public class BookmarkQuestions_Dialog extends DialogFragment {
    private Callback callback;

    RecyclerView recyclerViewBookmarks;
    DatabaseReference databaseReference;
    List<BookmarkData> list;
    static BookmarkQuestions_Dialog newInstance() {
        return new BookmarkQuestions_Dialog();
    }

    String playerId, playerName;
    SessionManager sessionManager;
    BookmarksAdapter bookmarksAdapter;
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_question_dialog, container, false);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        recyclerViewBookmarks = view.findViewById(R.id.recyclerViewBookmarks);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewBookmarks.setLayoutManager(layoutManager);


        sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getData();
        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);


        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bookmarksAdapter = new BookmarksAdapter(getActivity(), list);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                dismiss();
            }
        });


        databaseReference.child("placemenPaper_user_answer").child(playerId).child("06-01-2020").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String question_id = dataSnapshot1.getKey();
                    boolean bookmark_status = (boolean) dataSnapshot1.child("bookmark").getValue();
                    int position = (int) dataSnapshot1.child("position").getValue();
                    Log.e("bookmark_status", question_id + "  "+ bookmark_status);

                    list.add(new BookmarkData(question_id, bookmark_status, position));
                }

                recyclerViewBookmarks.setAdapter(bookmarksAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }
    public interface Callback {

        void onActionClick(String name);

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }


}
