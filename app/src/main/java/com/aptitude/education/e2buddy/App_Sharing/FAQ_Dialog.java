package com.aptitude.education.e2buddy.App_Sharing;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.Question.FeedbackFullScreenDialog;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQ_Dialog extends DialogFragment {

    FAQ_ListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Callback callback;
    AdView mAdView;

    public static FAQ_Dialog newInstance() {
        return new FAQ_Dialog();
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
        View view = inflater.inflate(R.layout.faq_dialog, container, false);
        TextView tvE2buddy = view.findViewById(R.id.tve2buddy);
        ImageButton closeDialog = view.findViewById(R.id.fullscreen_dialog_close);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tvE2buddy.setTypeface(type);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        prepareListData();

        listAdapter = new FAQ_ListAdapter(getActivity(), listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("How can I earn Coins ?");
        listDataHeader.add("How can I use the Coins ?");
        listDataHeader.add("What are Coins ?");


        List<String> earn_coins = new ArrayList<String>();
        earn_coins.add("Play Daily Quiz, Answer maximum correct questions and win Coins. Lead the monthly charts and earn extra coins. Also Share the App with your friends and Earn Coins.");

        List<String> coins = new ArrayList<String>();
        coins.add("Earn more Coins and Win Exciting Prizes. Click here for the Prizes.");

        List<String> e2coins = new ArrayList<String>();
        e2coins.add("Coins are Virtual Currency that you win for answering correct questions and Sharing the app with your Friends. 1 E2 Coin = 100 Points");

        listDataChild.put(listDataHeader.get(0), earn_coins);
        listDataChild.put(listDataHeader.get(1), coins);
        listDataChild.put(listDataHeader.get(2), e2coins);

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
