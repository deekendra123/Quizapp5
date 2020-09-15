package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryQuizAdapter extends RecyclerView.Adapter<SubCategoryQuizAdapter.SubCategoryHolder> {

    Context mCtx;
    List<CategoryData> list;
    OnItemClickListener listener;
    DatabaseReference databaseReference;


    public SubCategoryQuizAdapter(Context mCtx, List<CategoryData> list) {
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
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_quiz_item, parent, false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryHolder holder, int position) {

        final CategoryData categoryData = list.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
     /*   Toast.makeText(mCtx, "parent cat  "+ categoryData.getParent_category()+ "\n"+"child cat "+ categoryData.getChild_category()+
                "\n"+ "quiz id "+ categoryData.getQuiz_id(),Toast.LENGTH_SHORT).show();
     */



        if (categoryData.getChild_category().equals("Harbhajan Singh")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fharbhajan.jpg?alt=media&token=a45d6266-b9b2-407a-b5bb-48e9329f6e06")
                    .into(holder.imgSubCategroy);

           
        }
        else if (categoryData.getChild_category().equals("Kapil Dev")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fkapil.jpg?alt=media&token=1b6976f3-0946-41c7-ba59-23f460de0118")
                    .into(holder.imgSubCategroy);
        }
        else if (categoryData.getChild_category().equals("MS Dhoni")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fdhoni.jpg?alt=media&token=b3804959-2323-4230-a523-54f07d3f3429")
                    .into(holder.imgSubCategroy);
        }

        else if (categoryData.getChild_category().equals("Sachin Tendulkar")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fsachin.jpg?alt=media&token=25be3f45-13f5-48a5-bdee-0f56770c0b22")
                    .into(holder.imgSubCategroy);
        }

        else if (categoryData.getChild_category().equals("Sourav Ganguly")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fsourav.jpg?alt=media&token=f2818402-5a80-4ae6-8d0f-f358652f41ec")
                    .into(holder.imgSubCategroy);
        }

        else if (categoryData.getChild_category().equals("Virat Kohli")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fvirat.jpeg?alt=media&token=e006780c-24bb-4ba7-a2e2-309dc6f4cf5b")
                    .into(holder.imgSubCategroy);
        }

        holder.tvSubCategory.setText(""+categoryData.getChild_category());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("category_questions").child(categoryData.getParent_category()).child(categoryData.getChild_category()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count =  dataSnapshot.getChildrenCount();
                        if (count>1){
                            showAlertDialog(categoryData.getParent_category(),categoryData.getChild_category());
                        }
                        else {
                            System.gc();
                            Intent intent = new Intent(mCtx, StartCategoryQuizActivity.class);
                            intent.putExtra("parent_category",categoryData.getParent_category());
                            intent.putExtra("child_category", categoryData.getChild_category());
                            intent.putExtra("quiz_id", categoryData.getQuiz_id());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mCtx.startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {

        TextView tvSubCategory;
        ImageView imgSubCategroy;
        public SubCategoryHolder(View itemView) {
            super(itemView);
            tvSubCategory = itemView.findViewById(R.id.tvSubCategory);
            imgSubCategroy = itemView.findViewById(R.id.imgSubCategroy);

        }
    }


    public void showAlertDialog(final String parent_category, final String child_category) {

        final List<CategoryData> categoryDataList;
        Transformation transformation;
        View alertLayout = LayoutInflater.from(mCtx).inflate(R.layout.subcategory_layout_custom_dialog, null);
        final RecyclerView recyclerViewSubCategory = alertLayout.findViewById(R.id.recyclerViewSubCategory);
        final ImageView childCategoryImage =  alertLayout.findViewById(R.id.childCategoryImage);
        final TextView childCategoryName =  alertLayout.findViewById(R.id.childCategoryName);


        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubCategory.setLayoutManager(layoutManager3);

        categoryDataList = new ArrayList<>();

        transformation = new RoundedTransformationBuilder()
                .borderColor(mCtx.getResources().getColor(R.color.gray))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();


        childCategoryName.setText(""+child_category);

        if (child_category.equals("Harbhajan Singh")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fharbhajan.jpg?alt=media&token=a45d6266-b9b2-407a-b5bb-48e9329f6e06")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);
        }
        else if (child_category.equals("Kapil Dev")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fkapil.jpg?alt=media&token=1b6976f3-0946-41c7-ba59-23f460de0118")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);


        }
        else if (child_category.equals("MS Dhoni")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fdhoni.jpg?alt=media&token=b3804959-2323-4230-a523-54f07d3f3429")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }

        else if (child_category.equals("Sachin Tendulkar")){


            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fsachin.jpg?alt=media&token=25be3f45-13f5-48a5-bdee-0f56770c0b22")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }

        else if (child_category.equals("Sourav Ganguly")){


            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fsourav.jpg?alt=media&token=f2818402-5a80-4ae6-8d0f-f358652f41ec")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }

        else if (child_category.equals("Virat Kohli")){


            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fvirat.jpeg?alt=media&token=e006780c-24bb-4ba7-a2e2-309dc6f4cf5b")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }



        AlertDialog.Builder alert = new AlertDialog.Builder(mCtx, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();

        databaseReference.child("category_questions").child(parent_category).child(child_category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    int quiz_id = Integer.parseInt(dataSnapshot1.getKey());
                   // Log.e("dk ", String.valueOf(quiz_id));
                    categoryDataList.add(new CategoryData(parent_category, child_category, quiz_id));
                }
                CategoryQuizAdapter categoryQuizAdapter = new CategoryQuizAdapter(mCtx, categoryDataList, dialog);
                recyclerViewSubCategory.setAdapter(categoryQuizAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.show();
    }
}
