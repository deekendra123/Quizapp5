package com.aptitude.education.e2buddy.Internship;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.aptitude.education.e2buddy.Intro.SessionManager;
import com.aptitude.education.e2buddy.Menu.FAQ_Dialog;
import com.aptitude.education.e2buddy.Menu.FeedbackFullScreenDialog;
import com.aptitude.education.e2buddy.Menu.ShareAndEarnDialog;
import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.School_Quiz.StudentProfileActivity;
import com.aptitude.education.e2buddy.ViewData.InternshipData;
import com.aptitude.education.e2buddy.ViewData.InternshipRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Matrix on 07-01-2019.
 */

public class  InternshipDetailsBottemSheet extends BottomSheetDialogFragment  {


    ImageView userIcon;
    LinearLayout layout;
    String playerId, playerName, playerEmail, playerImageUrl, courseName, duration, image;
    SessionManager sessionManager;

    AppCompatButton btApply;
    DatabaseReference databaseReference;
    int i=1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.internship_bottem_dialog, container, false);
        btApply = view.findViewById(R.id.btApply);

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getData();

        playerId = user.get(SessionManager.KEY_PLAYER_ID);
        playerName = user.get(SessionManager.KEY_PLAYER_NAME);
        playerEmail = user.get(SessionManager.KEY_PLAYER_EMAIL);
        playerImageUrl = user.get(SessionManager.KEY_PLAYER_IMAGE_URL);

        Bundle bundle = getArguments();
        image = bundle.getString("imageUrl");
        databaseReference = FirebaseDatabase.getInstance().getReference();




        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // dismiss();
                showAlertDialog();

            }
        });



        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.internship_bottem_dialog,null,false);
       // Unbinder unbinder = ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);
        FrameLayout bottomSheet = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bglinear);

        super.setupDialog(dialog, style);
    }

    public void showAlertDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.intership_request_custom_dialog, null);

        final Button btsubmit = alertLayout.findViewById(R.id.btsubmit);
        final Spinner spName = alertLayout.findViewById(R.id.spName);
        final Spinner spMonth = alertLayout.findViewById(R.id.spMonth);
        final TextInputEditText etName = alertLayout.findViewById(R.id.etName);
        final TextInputEditText etEmail = alertLayout.findViewById(R.id.etEmail);
        final TextInputEditText etNumber = alertLayout.findViewById(R.id.etNumber);


        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

        alert.setView(alertLayout);


        selectName(spName);
        selectDuration(spMonth);

        final AlertDialog dialog = alert.create();

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().equals("")){
                    etName.setError("Enter Name");
                }

                else if(!isValidEmailId(etEmail.getText().toString().trim())){

                    etEmail.setError("Enter Valid Email");

                }else if (etNumber.getText().toString().length()!=10 ) {
                    etNumber.setError("Enter Number");
                }else if (courseName.equals("Select Internship")){

                    Toast.makeText(getActivity(), "Select Internship Course", Toast.LENGTH_SHORT).show();

                }else if (duration.equals("Select Internship Duration (in months)")){

                    Toast.makeText(getActivity(), "Select Internship Duration", Toast.LENGTH_SHORT).show();

                }
                else {
                    inserInternshipData(etName, etEmail, etNumber, courseName, duration, image);
                    dialog.dismiss();
                    dismiss();
                    Fragment fragment = new InternshipFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, fragment);
                    fragmentTransaction.commit();

                }
            }
        });

        dialog.show();
    }

    private void selectName(Spinner spName){

        List<String> categories = new ArrayList<String>();
        categories.add("Select Internship");
        categories.add("Android Development");
        categories.add("Web Development");
        categories.add("ERP Development");
        categories.add("iOS Development");
        categories.add("Wordpress Development");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spName.setAdapter(dataAdapter);

        spName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 courseName = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void selectDuration(Spinner spMonth){

        List<String> categories = new ArrayList<String>();
        categories.add("Select Internship Duration (in months)");
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(dataAdapter);


        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duration = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void inserInternshipData(TextInputEditText etName, TextInputEditText etEmail, TextInputEditText etNumber, String courseName, String duration, String image){




        InternshipRequest internshipData = new InternshipRequest(etName.getText().toString(),
                etEmail.getText().toString(), etNumber.getText().toString(), courseName, duration, image, "pending", "", "");



        databaseReference.child("internshipRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(playerId)){

                    Query lastQuery = databaseReference.child("internshipRequest").child(playerId).orderByKey().limitToLast(1);

                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                                int key = Integer.parseInt(dataSnapshot1.getKey());

                                i = key+1;

                                databaseReference.child("internshipRequest").child(playerId).child(String.valueOf(i)).setValue(internshipData);

                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    databaseReference.child("internshipRequest").child(playerId).child(String.valueOf(i)).setValue(internshipData);

                  }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }





    public void onDismiss(DialogInterface dialogInterface)
    {
        super.onDismiss(dialogInterface);

    }



}
