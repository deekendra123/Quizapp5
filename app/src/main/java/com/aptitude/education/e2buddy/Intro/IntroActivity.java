package com.aptitude.education.e2buddy.Intro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.Question.HomeNevActivity;
import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.School_Quiz.School_Code_Activity;
import com.aptitude.education.e2buddy.ViewData.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class IntroActivity extends AppCompatActivity{
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] mDots;

    private IntroAdapter introAdapter;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    Button signInButton, loginwithphone;
    private static final int RC_SIGN_IN = 234;

    private static final String TAG = "Quizapp";

    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth.AuthStateListener listener;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

      signInButton = findViewById(R.id.buttonSignIn);

  //    loginwithphone = findViewById(R.id.loginphone);

        CheckInternet checkInternet = new CheckInternet(getApplicationContext());
        checkInternet.checkConnection();

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        introAdapter = new IntroAdapter(getApplicationContext());

        viewPager.setAdapter(introAdapter);

        addDotIndicator(0);

        viewPager.addOnPageChangeListener(onPageChangeListener);

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                 if (firebaseAuth.getCurrentUser()!=null){

                    if (firebaseAuth.getCurrentUser()!=null) {
                        // User is signed in
                        Intent i = new Intent(IntroActivity.this, HomeNevActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), School_Code_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).
                        requestEmail().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
    /*    loginwithphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             // Intent intent = new Intent(getApplicationContext(), CopyDbActivity.class);
               Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);

                startActivity(intent);

            }
        });

*/
    }


    public void addDotIndicator(int position){

        mDots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i =0; i<mDots.length; i++){
            mDots[i] = new TextView(this );
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.dot_inactives));
            dotsLayout.addView(mDots[i]);
        }

        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(listener);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeNevActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                fireBaseAuthwithGoogle(account);

                if (!isFinishing()) {
                    progressDialog = new ProgressDialog(IntroActivity.this);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                }
            } catch (ApiException e) {
            }
        }
    }

    private void fireBaseAuthwithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {

                            if (user != null) {
                                // User is signed in
                                Intent i = new Intent(IntroActivity.this, HomeNevActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            else {

                                addData();
                                Intent intent = new Intent(IntroActivity.this, School_Code_Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();


                            }

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(IntroActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void addData(){

        final String username, userid, useremail,token_id,imageUri;
        final DatabaseReference databaseReference;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, "signInWithCredential:success");
        final FirebaseUser user = mAuth.getCurrentUser();
        userid = user.getUid();
        useremail = user.getEmail();
        username = user.getDisplayName();
        imageUri = user.getPhotoUrl().toString();

   //     sessionManager.createLoginSession(userid,username,useremail);

        token_id = FirebaseInstanceId.getInstance().getToken();
        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");
        final UserData data = new UserData(useremail, token_id, username,"0",imageUri);

        reference.child("user_info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user.getUid())){

                }
                else {
                    databaseReference.child(userid).setValue(data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toast.makeText(IntroActivity.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Alert");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IntroActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
