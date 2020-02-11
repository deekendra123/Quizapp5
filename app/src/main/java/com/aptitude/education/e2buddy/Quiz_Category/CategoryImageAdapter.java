package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.CategoryData;
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

public class CategoryImageAdapter extends PagerAdapter {

    private List<CategoryData> images;
    private LayoutInflater inflater;
    private Context context;
    DatabaseReference databaseReference;

    public CategoryImageAdapter(Context context, List<CategoryData> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.category_images, view, false);
        ImageView myImage = myImageLayout
                .findViewById(R.id.image);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final CategoryData categoryData = images.get(position);

        Picasso.get()
                .load(categoryData.getImageUrl())
                .into(myImage);


        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("category_questions").child(categoryData.getParent_category()).child(categoryData.getChild_category()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final long count = dataSnapshot.getChildrenCount();
                        if (count > 1) {
                            showAlertDialog(categoryData.getParent_category(), categoryData.getChild_category());
                        } else {

                            System.gc();
                            Intent intent = new Intent(context, StartCategoryQuizActivity.class);
                            intent.putExtra("parent_category",categoryData.getParent_category());
                            intent.putExtra("child_category", categoryData.getChild_category());
                            intent.putExtra("quiz_id", categoryData.getQuiz_id());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);

    }


    public void showAlertDialog(final String parent_category, final String child_category) {

        final List<CategoryData> categoryDataList;
        Transformation transformation;
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.subcategory_layout_custom_dialog, null);
        final RecyclerView recyclerViewSubCategory = alertLayout.findViewById(R.id.recyclerViewSubCategory);
        final ImageView childCategoryImage =  alertLayout.findViewById(R.id.childCategoryImage);
        final TextView childCategoryName =  alertLayout.findViewById(R.id.childCategoryName);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        categoryDataList = new ArrayList<>();

        transformation = new RoundedTransformationBuilder()
                .borderColor(context.getResources().getColor(R.color.gray))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        childCategoryName.setText(""+child_category);
        if (child_category.equals("Verbal Reasoning")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fwhatisit.jpg?alt=media&token=d2b87845-9788-483f-95ce-9858a464735d")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);
        }
        else if (child_category.equals("Space")){

            Picasso.get()
                    .load( "https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fspace.jpg?alt=media&token=45724e64-0223-49b9-b095-7de5d07effb3")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);
        }

        else if (child_category.equals("Technology")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Ftech.jpg?alt=media&token=636f0b2a-d4d5-40b3-83e6-d0214eddc35b")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);
        }


        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubCategory.setLayoutManager(layoutManager3);


        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();

        databaseReference.child("category_questions").child(parent_category).child(child_category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    int quiz_id = Integer.parseInt(dataSnapshot1.getKey());
                    Log.e("dk ", String.valueOf(quiz_id));
                    categoryDataList.add(new CategoryData(parent_category, child_category, quiz_id));
                    CategoryQuizAdapter categoryQuizAdapter = new CategoryQuizAdapter(context, categoryDataList,dialog);
                    recyclerViewSubCategory.setAdapter(categoryQuizAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.show();
    }

}
