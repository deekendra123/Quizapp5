package com.aptitude.education.e2buddy.AdMob;

import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Banner_Ad {

    Context context;
    AdView mAdView;

    public Banner_Ad(Context context, AdView mAdView) {
        this.context = context;
        this.mAdView = mAdView;
    }


    public void showBannerAd(){

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}
