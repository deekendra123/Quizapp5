package com.aptitude.education.e2buddy.School_Quiz;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.ImageData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.Calendar;
public class Student_Details_Activity extends AppCompatActivity {

    EditText etStudentName;
    TextView etDateofBirth,etSchoolName;
    ImageView imageView,btDone,imgadate;
    Transformation transformation;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    DatabaseReference mDatabaseRef,databaseReference;
    FirebaseAuth auth;
    DatePickerDialog datePickerDialog;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__details_);

        etStudentName = findViewById(R.id.editText11);
        etSchoolName = findViewById(R.id.editText13);
        etDateofBirth = findViewById(R.id.editText12);
        btDone = findViewById(R.id.button8);
        imageView = findViewById(R.id.userimage);
        imgadate = findViewById(R.id.imageView1);


        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference("user_Images/"+userid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        databaseReference.child("user_info").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String s_code = dataSnapshot.child("school_code").getValue().toString();

                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.userimg)
                            .fit()
                            .transform(transformation)
                            .into(imageView);

                    getSchoolName(s_code);

                    //  Toast.makeText(getApplicationContext(), ""+ imageUrl,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });


        etDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(Student_Details_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etDateofBirth.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        imgadate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(Student_Details_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etDateofBirth.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mImageUri==null) {

                    mDatabaseRef.child("user_info").child(userid).child("student_name").setValue(etStudentName.getText().toString());
                    mDatabaseRef.child("user_info").child(userid).child("dob").setValue(etDateofBirth.getText().toString());
                    mDatabaseRef.child("user_info").child(userid).child("school_name").setValue(etSchoolName.getText().toString());


                    if (etStudentName.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Enter your name and Proceed",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        System.gc();
                        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    try {
                        if (etStudentName.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Enter your name and Proceed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            uploadFile();
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void getSchoolName(String schoolcode) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("school_codes").child(schoolcode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String s_name = dataSnapshot.child("school_name").getValue().toString();
                    etSchoolName.setText(s_name);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .placeholder(R.drawable.userimg)
                    .fit()
                    .transform(transformation)
                    .into(imageView);
        }
    }


    private void uploadFile() {

        final ProgressDialog progressDialog = new ProgressDialog(Student_Details_Activity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));


        mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFinishing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }, 500);

                        ImageData upload = new ImageData(taskSnapshot.getDownloadUrl().toString());

                        mDatabaseRef.child("user_info").child(userid).child("image_Url").setValue(taskSnapshot.getDownloadUrl().toString());
                        mDatabaseRef.child("user_info").child(userid).child("student_name").setValue(etStudentName.getText().toString());
                        mDatabaseRef.child("user_info").child(userid).child("dob").setValue(etDateofBirth.getText().toString());
                        mDatabaseRef.child("user_info").child(userid).child("school_name").setValue(etSchoolName.getText().toString());


                        Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();



                        System.gc();
                        Intent intent = new Intent(getApplicationContext(), HomeNevActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploading...");
                    }
                });

    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Enter name and click Submit",
                    Toast.LENGTH_LONG).show();
        return false;
    }


}
