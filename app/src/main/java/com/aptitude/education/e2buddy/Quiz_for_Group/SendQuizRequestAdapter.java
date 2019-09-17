package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.NotificationData;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matrix on 21-02-2019.
 */

public class SendQuizRequestAdapter extends RecyclerView.Adapter<SendQuizRequestAdapter.SendRequestHolder> {

    Context mCtx;
    List<OnlineStatusData> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate,username,id;
    OnItemClickListener listener;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    int min = 10;
    int max = 100;
    int randomquizid;
    int itemcount = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor1;

    Button playquiz;

    public SendQuizRequestAdapter(Context mCtx, List<OnlineStatusData> list, Button playquiz) {
        this.mCtx = mCtx;
        this.list = list;
        this.playquiz = playquiz;
    }


    public interface OnItemClickListener
    {
        void onItemClick(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public SendRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.online_user_list_for_group, parent, false);
        return new SendRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SendRequestHolder holder, int position) {

         databaseReference = FirebaseDatabase.getInstance().getReference("group_notifications");
         databaseReference2 = FirebaseDatabase.getInstance().getReference();

        id = databaseReference.push().getKey();

        preferences = mCtx.getSharedPreferences("groupnotification", Context.MODE_PRIVATE);
        editor1 = preferences.edit();
        editor1.putString("group_notification", id);
        editor1.commit();


        OnlineStatusData onlineStatusData = list.get(position);

        sharedPreferences = mCtx.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        holder.username.setText("" + onlineStatusData.getUser_name());

        randomquizid =  new Random().nextInt((max - min) + 1) + min;

        databaseReference2.child("quiz_request_accepted_users").child(id).child(userid).child("status").setValue(Boolean.TRUE);


        final String useremail = list.get(position).getUser_email();
        final String user_name = list.get(position).getUser_name();
        final boolean status = list.get(position).isOnline_status();
        final String user_id = list.get(position).getUser_id();



        holder.itemView.setClickable(false);

        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                itemcount++;

                if (itemcount>=3){
                    playquiz.setVisibility(View.VISIBLE);
                }

                if (itemcount<5){
                    sendNotification(user_id,user_name);
                    Toast.makeText(mCtx, "Request has been sent to "+ user_name,Toast.LENGTH_SHORT).show();
                }
                else {
                    holder.request.setClickable(false);

                    Toast.makeText(mCtx, "You can send only 5 Quiz Request at a time",Toast.LENGTH_SHORT).show();
                }
            }
        });



        playquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("group_Question_id");
                Random random = new Random();
                for (int i = 0; i<10; i++){

                    int max = random.nextInt(11-1+1)+1;
                    databaseReference.child(id).child(String.valueOf(max)).setValue(i);
                }

                Intent intent = new Intent(mCtx, RequestAcceptedUsersActivity.class);
                intent.putExtra("group_notification_id", id);
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        int arr =0;

        try {
            if (list.size()==0){
                arr = 0;
            }
            else {
                arr = list.size();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return arr;

    }

    public void setFilter(List<OnlineStatusData> dataList){
        list = new ArrayList<>();
        list.addAll(dataList);
        notifyDataSetChanged();
    }

    public class SendRequestHolder extends RecyclerView.ViewHolder{

        TextView username;
        Button request;
        Button circleButton;
        public SendRequestHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvusername);
            circleButton = itemView.findViewById(R.id.btgreen);
            request = itemView.findViewById(R.id.btrequest);

        }


    }

    private void sendNotification(String user_id, String name){

        NotificationData notificationData = new NotificationData(userid,username);

       databaseReference.child(id).child(user_id).child("sender_name").child(username).child(userid).setValue(notificationData);

    }

}
