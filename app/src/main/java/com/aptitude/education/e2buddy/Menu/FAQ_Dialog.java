package com.aptitude.education.e2buddy.Menu;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aptitude.education.e2buddy.GoogleAds.BannerAd;
import com.aptitude.education.e2buddy.Intro.CheckInternet;
import com.aptitude.education.e2buddy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQ_Dialog extends DialogFragment {

    FAQ_ListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Callback callback;

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
        expListView = view.findViewById(R.id.lvExp);

        BannerAd bannerAd = new BannerAd(getActivity(), view);
        bannerAd.loadFragmentBannerAd();


        CheckInternet checkInternet = new CheckInternet(getActivity());
        checkInternet.checkConnection();

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
