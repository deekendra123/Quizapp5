package com.aptitude.education.e2buddy.Menu;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aptitude.education.e2buddy.GoogleAds.BannerAd;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;

public class FeedbackFullScreenDialog extends DialogFragment {
    private Callback callback;

    static FeedbackFullScreenDialog newInstance() {
        return new FeedbackFullScreenDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_dialog, container, false);
        ImageView btSendFeedback = view.findViewById(R.id.sendfeedback);
        TextView tvname = view.findViewById(R.id.t1);
        TextView tvemail = view.findViewById(R.id.t2);
        TextView tvmsg = view.findViewById(R.id.t3);
        TextView tvE2buddy = view.findViewById(R.id.tve2buddy);
        final EditText etName = view.findViewById(R.id.editText11);
        final EditText etEmail = view.findViewById(R.id.editText12);
        final EditText etMsg = view.findViewById(R.id.editText13);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);

        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        BannerAd bannerAd = new BannerAd(getActivity(), view);
        bannerAd.loadFragmentBannerAd();


        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.gc();
                dismiss();
            }
        });

        btSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name, email, messgae;
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                messgae = etMsg.getText().toString();


                if (name.isEmpty()) {
                    etName.setError("Name is required");
                    etName.requestFocus();
                    return;
                }
                else if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }
                else if (messgae.isEmpty()){
                    etMsg.setError("Message is required");
                    etMsg.requestFocus();
                    return;
                }
                else {

                    ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    Email email1 = new Email("e2buddy.quiz@gmail.com",
                            "E2buddy@123", email, messgae, "e2buddy.quiz@gmail.com", "e2buddy.quiz@gmail.com", name);
                    boolean success = email1.success();

                    if (success == true) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Your Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        return view;
    }
    public interface Callback {

        void onActionClick(String name);

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        System.gc();
    }
}
