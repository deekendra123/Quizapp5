package com.aptitude.education.e2buddy.Intro;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import com.aptitude.education.e2buddy.R;
import com.firebase.client.Firebase;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class Quizapp extends Application {

    public static final String CHANNEL_ID = "e2buddy";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        Firebase.setAndroidContext(getApplicationContext());

        TypefaceUtil.overrideFont(getApplicationContext(), "Roboto-Regular", "fonts/Roboto-Regular.ttf");

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "E2-buddy",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            final NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }


}
