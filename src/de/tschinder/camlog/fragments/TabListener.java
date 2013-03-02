package de.tschinder.camlog.fragments;

import de.tschinder.camlog.data.LogEntryAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

public class TabListener<T extends Fragment> implements ActionBar.TabListener
{
    private ListFragment mFragment;
    private final FragmentActivity mActivity;
    private final Class<T> mClass;

    /**
     * Constructor used each time a new tab is created.
     * 
     * @param activity
     *            The host Activity, used to instantiate the fragment
     * @param tag
     *            The identifier tag for the fragment
     * @param clz
     *            The fragment's Class, used to instantiate the fragment
     */
    public TabListener(FragmentActivity activity, Class<T> clz)
    {
        mActivity = activity;
        mClass = clz;
    }

    /* The following are each of the ActionBar.TabListener callbacks */
    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction unused)
    {
        FragmentManager fragMgr = mActivity.getSupportFragmentManager();
        FragmentTransaction xaction = fragMgr.beginTransaction();

        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = (ListFragment) Fragment.instantiate(mActivity, mClass.getName());
            xaction.replace(android.R.id.content, mFragment, mClass.getName());
        } else {
            // If it exists, simply attach it in order to show it
            xaction.attach(mFragment);
        }
        xaction.commit();
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft)
    {
        if (mFragment != null) {
            FragmentManager fragMgr = mActivity.getSupportFragmentManager();
            FragmentTransaction xaction = fragMgr.beginTransaction();

            xaction.detach(mFragment);
            xaction.commit();
        }
    }
    
    public void refreshTab()
    {
        if (mFragment != null) {
            LogEntryAdapter adapter = (LogEntryAdapter) mFragment.getListAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft)
    {
        // User selected the already selected tab. Usually do nothing.
    }
}