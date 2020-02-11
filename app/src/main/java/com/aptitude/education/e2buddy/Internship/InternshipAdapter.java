package com.aptitude.education.e2buddy.Internship;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Menu.MenuSheetDialog;
import com.aptitude.education.e2buddy.Placement_Papers.BookmarksAdapter;
import com.aptitude.education.e2buddy.Placement_Papers.Quiz_Instruction_Activity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.InternshipData;
import com.aptitude.education.e2buddy.ViewData.InternshipRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.PaperHolder> {

    Context mCtx;
    List<InternshipData> list;

    OnItemClickListener listener;
    String playerId, playerName, playerEmail, playerImageUrl, courseName, duration;
    SessionManager sessionManager;



    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public InternshipAdapter(Context mCtx, List<InternshipData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public PaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.internship_list_items, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHolder holder, int position) {
        InternshipData internshipData = list.get(position);
        holder.tvInternshipName.setText(""+internshipData.getName());
        holder.tvRemainingSeat.setText("Remaining Slots : "+internshipData.getRemainingSlots());
        holder.tvTotalSeat.setText("Total Slots : "+internshipData.getTotalSlots());

        sessionManager = new SessionManager(mCtx);

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);


        Picasso.get().load(internshipData.getImageUrl()).into(holder.imgLogo);


        if (internshipData.getLogo().equals("A")){

            holder.relativelayout.setBackgroundResource(R.drawable.bgcard10);


        }else if (internshipData.getLogo().equals("W")){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard6);


        }else if (internshipData.getLogo().equals("E")){

            holder.relativelayout.setBackgroundResource(R.drawable.bgcard7);


        }else if (internshipData.getLogo().equals("I")){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard8);


        }else if (internshipData.getLogo().equals("Wp")){
            holder.relativelayout.setBackgroundResource(R.drawable.bgcard9);


        }

        holder.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InternshipData internshipData = list.get(position);
                InternshipDetailsBottemSheet internshipDetailsBottemSheet = new InternshipDetailsBottemSheet();
                internshipDetailsBottemSheet.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", internshipData.getImageUrl());
                internshipDetailsBottemSheet.setArguments(bundle);
                internshipDetailsBottemSheet.show(((FragmentActivity)mCtx).getSupportFragmentManager(), "quizapp2");


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaperHolder extends RecyclerView.ViewHolder {

        TextView tvInternshipName,tvRemainingSeat,tvTotalSeat;
        AppCompatButton btApply;
        ImageView imgLogo;
        RelativeLayout relativelayout;

        public PaperHolder(View itemView) {
            super(itemView);
            tvInternshipName = itemView.findViewById(R.id.tvInternshipName);
            tvRemainingSeat = itemView.findViewById(R.id.tvRemainingSeat);
            btApply = itemView.findViewById(R.id.btApply);
            tvTotalSeat = itemView.findViewById(R.id.tvTotalSeat);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            relativelayout = itemView.findViewById(R.id.relativelayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView,position);
                        }
                    }
                }
            });

        }
    }

}
