package com.aptitude.education.e2buddy.One_on_One_Quiz_Challenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.NotificationData;
import com.aptitude.education.e2buddy.ViewData.OnlineStatusData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Matrix on 21-02-2019.
 */

public class SendRequestToOpponentAdapter extends RecyclerView.Adapter<SendRequestToOpponentAdapter.SendRequestHolder> {

    Context mCtx;
    List<OnlineStatusData> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String quizid,quizname,userid,quizdate,username,id;
    OnItemClickListener listener;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;
    String status;
    CountDownTimer countDownTimer ;
    int min = 10;
    int max = 100;
    int randomquizid;

    public SendRequestToOpponentAdapter(Context mCtx, List<OnlineStatusData> list) {
        this.mCtx = mCtx;
        this.list = list;
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

        View view = LayoutInflater.from(mCtx).inflate(R.layout.online_user_list, parent, false);
        return new SendRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SendRequestHolder holder, final int position) {

        OnlineStatusData onlineStatusData = list.get(position);

        sharedPreferences = mCtx.getSharedPreferences("quizpref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        quizid = sharedPreferences.getString("quizid", "");
        quizname = sharedPreferences.getString("quizname", "");
        userid = sharedPreferences.getString("userid", "");
        quizdate = sharedPreferences.getString("quizdate","");
        username = sharedPreferences.getString("username","");

        holder.username.setText("" + onlineStatusData.getUser_name());

            Picasso.get()
                    .load(onlineStatusData.getImageUrl())
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(holder.transformation)
                    .into(holder.playerImage);

        randomquizid =  new Random().nextInt((max - min) + 1) + min;

        //final String useremail = list.get(position).getUser_email();
        final boolean status = list.get(position).isOnline_status();
        final String user_id = list.get(position).getUser_id();

        holder.itemView.setClickable(false);

        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_name = list.get(position).getUser_name();

                // Toast.makeText(mCtx, "name "+ user_name,Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat hour = new SimpleDateFormat("HH");
                SimpleDateFormat minut = new SimpleDateFormat("mm");
                SimpleDateFormat second = new SimpleDateFormat("ss");
                String hr = hour.format(calendar.getTime());
                String min = minut.format(calendar.getTime());
                String sec = second.format(calendar.getTime());

                sendNotification(user_id,user_name, hr, min, sec );
                Toast.makeText(mCtx, "Request has been sent to "+ user_name,Toast.LENGTH_SHORT).show();

                showAlertDialog(user_id,id,user_name);


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

    public class SendRequestHolder extends RecyclerView.ViewHolder{

        TextView username;
        Transformation transformation;
        TextView request;
        ImageView playerImage;
        Button circleButton;
        public SendRequestHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvusername);
            circleButton = itemView.findViewById(R.id.btgreen);
            request = itemView.findViewById(R.id.btrequest);
            playerImage = itemView.findViewById(R.id.img);

            Typeface type = Typeface.createFromAsset(mCtx.getAssets(), "fonts/Roboto-Regular.ttf");
            username.setTypeface(type);
            request.setTypeface(type);

            transformation = new RoundedTransformationBuilder()
                    .borderColor(mCtx.getResources().getColor(R.color.gray))
                    .borderWidthDp(0)
                    .cornerRadiusDp(50)
                    .oval(false)
                    .build();

        }


    }

    private void sendNotification(String user_id, String name, String hr, String min, String sec){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_notifications");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();


        DatabaseReference dr= FirebaseDatabase.getInstance().getReference("1V1_Question_id");

        id = databaseReference.push().getKey();

        NotificationData notificationData = new NotificationData(userid,username,hr,min,sec);

        databaseReference.child(user_id).child(id).child("sender_name").child(username).child(userid).setValue(notificationData);

    }

    public void filterList(ArrayList<OnlineStatusData> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    private void showAlertDialog(final String user_id, final String notification_id, final String username){

        final DatabaseReference ref;

        ref = FirebaseDatabase.getInstance().getReference();


        final LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_notification_response, null);
        final TextView userrank = alertLayout.findViewById(R.id.userrank);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final TextView tvcancel = alertLayout.findViewById(R.id.tvcancel);

        final  ImageView img1 = alertLayout.findViewById(R.id.dot1);
        final  ImageView img2 = alertLayout.findViewById(R.id.dot2);
        final  ImageView img3 = alertLayout.findViewById(R.id.dot3);


        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(mCtx, R.anim.fade_anim);
        img1.startAnimation(myFadeInAnimation);
        img2.startAnimation(myFadeInAnimation);
        img3.startAnimation(myFadeInAnimation);

        msg.setText("Waiting for "+username+" to join the game");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mCtx, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();

                databaseReference3.child("user_notifications").child(user_id).child(notification_id).child("cancel_status").setValue("yes");

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new SendRequestToOpponentFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();

                dialog.dismiss();
                countDownTimer.cancel();
            }
        });
        countDownTimer =  new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                userrank.setText("" +millisUntilFinished / 1000);


            }

            public void onFinish() {
                dialog.dismiss();
                Toast.makeText(mCtx, ""+username+" is not available at the moment. Try Again letter.",Toast.LENGTH_SHORT).show();
            }
        }.start();

        ref.child("user_notifications").child(user_id).child(notification_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    status = dataSnapshot.child("status").getValue().toString();
                    // Toast.makeText(mCtx, "status : "+ status,Toast.LENGTH_SHORT).show();

                    if (status.equals("yes")){

                        dialog.dismiss();
                        countDownTimer.cancel();

                        showAlert(user_id,notification_id,username);
                    }
                    else if (status.equals("no")){
                        dialog.dismiss();
                        countDownTimer.cancel();

                        Toast.makeText(mCtx, username+" is not available for a game at the moment.",Toast.LENGTH_SHORT).show();

                      /*  AppCompatActivity activity = mCtx.getContext();
                        Fragment myFragment = new SendRequestToOpponentFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();
*/
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dialog.show();
    }

    private void showAlert(final String user_id, final String notification_id, final String receiver_name){

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("user_notifications");
        final DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("1V1_Question_id");
            Animation zoomout;

        final LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View alertLayout = inflater.inflate(R.layout.layout_custom, null);
        final TextView msg = alertLayout.findViewById(R.id.hj);
        final Button startQuiz= alertLayout.findViewById(R.id.startgame);
        final ImageView smileFace = alertLayout.findViewById(R.id.smileface);

        zoomout = AnimationUtils.loadAnimation(mCtx, R.anim.zoomin);
        smileFace.setAnimation(zoomout);



        msg.setText(receiver_name+" has joined the game");

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mCtx, R.style.CustomDialogTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final android.app.AlertDialog dialog = alert.create();
        dialog.show();

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                databaseReference1.child(user_id).child(notification_id).child("play_status").setValue(true);

                Random random = new Random();
                for (int i = 0; i<10; i++){

                    int max = random.nextInt(416-1+1)+1;
                    databaseReference.child(notification_id).child(String.valueOf(max)).setValue(i);

                }

                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
                databaseReference2.child("1V1_Question_id").child(notification_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            long count = dataSnapshot.getChildrenCount();
                            if (count<10){
                                databaseReference.child("1V1_Question_id").child(notification_id).removeValue();

                                Random random = new Random();
                                for (int i = 0; i<10; i++){

                                    int max = random.nextInt(416-1+1)+1;
                                    databaseReference.child(notification_id).child(String.valueOf(max)).setValue(i);

                                }

                            } else {
                                sharedPreferences1 = mCtx.getSharedPreferences("receiver_info", Context.MODE_PRIVATE);
                                editor1 = sharedPreferences1.edit();
                                editor1.putString("notification_ids", notification_id);
                                editor1.putString("receiver_id", user_id);
                                editor1.putString("receiver_name",receiver_name);

                                editor1.commit();

                                Intent intent = new Intent(mCtx, LoaderForOneonOneActivity.class);
                                mCtx.startActivity(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });



    }

}
