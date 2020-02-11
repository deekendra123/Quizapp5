package com.aptitude.education.e2buddy.Question;

import android.content.Context;
import android.widget.ImageView;

import com.aptitude.education.e2buddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class GetPlayerImage {
    Context context;
    ImageView imageView;
    Transformation transformation;
    DatabaseReference databaseReference;
    String imageUrl, playerId;

    public GetPlayerImage(Context context, ImageView imageView, DatabaseReference databaseReference, String playerId) {
        this.context = context;
        this.imageView = imageView;
        this.databaseReference = databaseReference;
        this.playerId = playerId;
    }


    public void getImage(){

        transformation = new RoundedTransformationBuilder()
                .borderColor(context.getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();



        databaseReference.child("user_info").child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    imageUrl = dataSnapshot.child("image_Url").getValue(String.class);

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(imageView);

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
}
