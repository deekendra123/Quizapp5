package com.aptitude.education.e2buddy.Question;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aptitude.education.e2buddy.Quiz_Category.QuizCategoriesFragment;

public class QuizHomeFragmentPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public QuizHomeFragmentPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DailyDietFragment dailyDietFragment = new DailyDietFragment();
                return dailyDietFragment;
            case 1:
                QuizCategoriesFragment quizCategoriesFragment = new QuizCategoriesFragment();
                return quizCategoriesFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
