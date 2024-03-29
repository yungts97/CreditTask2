package com.example.credittask2;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private String titles[] = {"NOW SHOWING", "UPCOMING MOVIES", "WATCH LIST"};

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return ShowingFragment.newInstance(GlobalVariables.showingMovies);
            case 1:
                return ComingFragment.newInstance(GlobalVariables.upcomingMovies);
            case 2:
                return WatchFragment.newInstance(GlobalVariables.watchListMovies);
                default:
                    return null;

        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
