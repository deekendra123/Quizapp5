package com.aptitude.education.e2buddy.Intro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckInternet {

    Context mctx;

    public CheckInternet(Context mctx) {
        this.mctx = mctx;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)mctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection(){
        if(isOnline()==false){
            Toast.makeText(mctx, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }}
