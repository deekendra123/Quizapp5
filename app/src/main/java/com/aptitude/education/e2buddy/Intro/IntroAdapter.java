package com.aptitude.education.e2buddy.Intro;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aptitude.education.e2buddy.R;

/**
 * Created by Matrix on 04-01-2019.
 */

public class IntroAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public IntroAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {

            R.drawable.img1png,
            R.drawable.img2png,
            R.drawable.img3png,

    };

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_slider_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imageView2);
     //   TextView textView = view.findViewById(R.id.textView3);

       imageView.setImageResource(slide_images[position]);
       // textView.setText(slide_heading[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);
    }
}
