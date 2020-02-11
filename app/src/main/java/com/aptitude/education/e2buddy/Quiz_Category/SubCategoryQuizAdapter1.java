package com.aptitude.education.e2buddy.Quiz_Category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
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
import com.aptitude.education.e2buddy.ViewData.CategoryData1;
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

public class SubCategoryQuizAdapter1 extends RecyclerView.Adapter<SubCategoryQuizAdapter1.SubCategoryHolder> {

    Context mCtx;
    List<CategoryData1> list;
    OnItemClickListener listener;
    DatabaseReference databaseReference;


    public SubCategoryQuizAdapter1(Context mCtx, List<CategoryData1> list) {
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryHolder holder, int position) {

        final CategoryData1 categoryData = list.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        holder.tvSubCategory.setText(""+categoryData.getChild_category());
        if (categoryData.getChild_category().equals("Amitabh Bachchan")){

            holder.imgSubCategroy.setImageResource(R.drawable.amitabh);

        }

        else if (categoryData.getChild_category().equals("Deepika Padukone")){

            holder.imgSubCategroy.setImageResource(R.drawable.deepika);


        }

        else if (categoryData.getChild_category().equals("Shahrukh Khan")){
            holder.imgSubCategroy.setImageResource(R.drawable.shahrukh);

        }


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

        categoryDataList = new ArrayList<>();

        transformation = new RoundedTransformationBuilder()
                .borderColor(mCtx.getResources().getColor(R.color.gray))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        childCategoryName.setText(""+child_category);

        if (child_category.equals("Amitabh Bachchan")){


            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Famitabh.jpg?alt=media&token=242d275b-6baf-4f0a-8a39-eb12b90ff328")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }

        else if (child_category.equals("Deepika Padukone")){


            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2FDeepika.jpg?alt=media&token=50c89741-55e0-4c77-8f96-c04202e4bbde")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }

        else if (child_category.equals("Shahrukh Khan")){

            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/e2buddy.appspot.com/o/category_image%2Fshahrukh.jpg?alt=media&token=ef46b681-932c-45f7-bda4-8d9014d801aa")
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(childCategoryImage);

        }


        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSubCategory.setLayoutManager(layoutManager3);


        AlertDialog.Builder alert = new AlertDialog.Builder(mCtx, R.style.CustomDialogTheme);

        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();

        databaseReference.child("category_questions").child(parent_category).child(child_category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    int quiz_id = Integer.parseInt(dataSnapshot1.getKey());
                    categoryDataList.add(new CategoryData(parent_category, child_category, quiz_id));
                    CategoryQuizAdapter categoryQuizAdapter = new CategoryQuizAdapter(mCtx, categoryDataList,dialog);
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
