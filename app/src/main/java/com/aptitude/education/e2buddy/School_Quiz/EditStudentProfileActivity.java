package com.aptitude.education.e2buddy.School_Quiz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Calendar;

public class EditStudentProfileActivity extends AppCompatActivity {

    EditText etuserName ;
    TextView tvDob, tvstudentEmail,schoolcode;
    Button updateProfile;
    ImageView userIcon;
    FirebaseAuth auth;
    String userid;
    DatabaseReference databaseReference;
    Transformation transformation;
    ProgressDialog progressDialog;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    DatabaseReference mDatabaseRef;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);
        ImageButton closeDialog = findViewById(R.id.fullscreen_dialog_close);
        userIcon = findViewById(R.id.userIcon);
        updateProfile = findViewById(R.id.updateProfile);
        tvDob = findViewById(R.id.userDoB);
        etuserName = findViewById(R.id.etuserName);
        tvstudentEmail = findViewById(R.id.userEmail);
        schoolcode = findViewById(R.id.schoolcode);

        progressDialog = new ProgressDialog(EditStudentProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data Loading...");
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("user_Images/"+userid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(0)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        getUserProfile();

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(EditStudentProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tvDob.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        schoolcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showAlertDialog(userid);
            }
        });



        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mImageUri==null) {

                    if (etuserName.getText().toString().equals("")){
                        Toast.makeText(EditStudentProfileActivity.this, "Enter Student name and Proceed",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        mDatabaseRef.child("user_info").child(userid).child("student_name").setValue(etuserName.getText().toString());
                        mDatabaseRef.child("user_info").child(userid).child("dob").setValue(tvDob.getText().toString());
                        Intent intent = new Intent(EditStudentProfileActivity.this, StudentProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    try {
                        if (etuserName.getText().toString().equals("")){
                            Toast.makeText(EditStudentProfileActivity.this, "Enter Student name and Proceed",Toast.LENGTH_SHORT).show();
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
                    .into(userIcon);
        }
    }


    private void uploadFile() {

        final ProgressDialog progressDialog = new ProgressDialog(EditStudentProfileActivity.this);
        progressDialog.setTitle("Updating Your Profile...");
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
                                progressDialog.dismiss();
                            }
                        }, 500);

                        mDatabaseRef.child("user_info").child(userid).child("image_Url").setValue(taskSnapshot.getDownloadUrl().toString());
                        mDatabaseRef.child("user_info").child(userid).child("student_name").setValue(etuserName.getText().toString());
                        mDatabaseRef.child("user_info").child(userid).child("dob").setValue(tvDob.getText().toString());


                        Toast.makeText(EditStudentProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();


                        System.gc();

                        Intent intent = new Intent(EditStudentProfileActivity.this, StudentProfileActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditStudentProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        progressDialog.setMessage("Updating...");
                    }
                });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void getUserProfile() {

        databaseReference.child("user_info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String student_name = dataSnapshot.child("student_name").getValue().toString();
                    String imageUrl = dataSnapshot.child("image_Url").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue().toString();

                    if (dataSnapshot.hasChild("dob")){
                        String dob = dataSnapshot.child("dob").getValue().toString();
                        if (!dob.equals("")){
                            tvDob.setText(""+dob);

                        }else {
                            tvDob.setHint("Date of Birth");
                        }

                    }
                    else {
                        tvDob.setHint("Date of Birth");
                    }


                    etuserName.setText("" + student_name);
                    tvstudentEmail.setText(""+userEmail);
                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .transform(transformation)
                            .into(userIcon);

                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void showAlertDialog(final String userid) {

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_dailog_school_code, null);
        final EditText et_code = alertLayout.findViewById(R.id.userSchoolCode);
        final TextView save = alertLayout.findViewById(R.id.save);
        final TextView cancel = alertLayout.findViewById(R.id.cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(EditStudentProfileActivity.this, R.style.CustomDialogTheme);

        alert.setView(alertLayout);


        final AlertDialog dialog = alert.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("school_codes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            String school_code = dataSnapshot1.getKey();
                            if (et_code.getText().toString().equalsIgnoreCase(school_code)){
                                String school_code1 = school_code.toUpperCase();
                                databaseReference.child("user_info").child(userid).child("school_code").setValue(school_code1);
                                databaseReference.child("school_codes").child(school_code).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            String s_name = dataSnapshot.child("school_name").getValue().toString();
                                            databaseReference.child("user_info").child(userid).child("school_name").setValue(s_name);
                                            Toast.makeText(getApplicationContext(), "School Code updated successfully", Toast.LENGTH_SHORT).show();


                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                System.gc();
                               dialog.dismiss();

                            }
                            else {
                                       //Toast.makeText(getApplicationContext(), "Invalid School Code", Toast.LENGTH_SHORT).show();
                                       et_code.setError("Invalid School Code");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}
