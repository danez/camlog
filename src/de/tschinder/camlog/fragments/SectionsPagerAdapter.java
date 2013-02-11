package de.tschinder.camlog.fragments;

import java.util.ArrayList;

import de.tschinder.camlog.R;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    protected ArrayList<Fragment> Items = new ArrayList<Fragment>();

    protected final int NUMBER_TABS = 2;

    protected Activity activity;

    public SectionsPagerAdapter(FragmentManager fm, Activity activity)
    {
        super(fm);
        Items.add(new StartFragment());
        Items.add(new StartFragment());
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return new StartFragment();
            case 1:
                return new StartFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return NUMBER_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position) {
            case 0:
                return activity.getString(R.string.title_start_fragment);
            case 1:
                return activity.getString(R.string.title_history_fragment);
            default:
                return null;
        }
    }
}