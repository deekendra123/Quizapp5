package com.aptitude.education.e2buddy.GoogleAds;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BannerAd {
    private Context mCtx;
    private AdView mAdView;
    private View view;



    public BannerAd(Context mCtx, View view) {
        this.mCtx = mCtx;
        this.view = view;
    }

    public BannerAd(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void loadFragmentBannerAd(){
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    public void loadActivityBannerAd(){
        mAdView = (AdView) ((Activity)mCtx).findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



}