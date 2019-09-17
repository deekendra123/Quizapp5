package com.aptitude.education.e2buddy.AdMob;

import android.app.Activity;
import android.content.Context;

import com.aptitude.education.e2buddy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {

    Context context;
    static InterstitialAd interstitialAd;

    public AdManager(Context context) {
        this.context = context;
    }

    public void createAd() {

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.Interstitial_ad));

        AdRequest adRequest = new AdRequest.Builder().build();

        interstitialAd.loadAd(adRequest);
    }

    public InterstitialAd getAd() {
        return interstitialAd;
    }
}
