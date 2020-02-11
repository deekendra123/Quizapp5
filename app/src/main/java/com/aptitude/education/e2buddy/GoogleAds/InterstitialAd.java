package com.aptitude.education.e2buddy.GoogleAds;

import android.content.Context;
import android.view.View;

import com.aptitude.education.e2buddy.DisplayAnswer.ResultActivity;
import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class InterstitialAd {
    private Context mCtx;

    public InterstitialAd(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void loadInterstitialAd(){
        com.google.android.gms.ads.InterstitialAd mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(mCtx);

        mInterstitialAd.setAdUnitId(mCtx.getString(R.string.Interstitial_ad));

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }
}
