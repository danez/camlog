package de.tschinder.camlog.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.database.Helper;
import de.tschinder.camlog.fragments.ListAllFragment;
import de.tschinder.camlog.fragments.ListBusinessFragment;
import de.tschinder.camlog.fragments.ListPrivateFragment;
import de.tschinder.camlog.fragments.ListWorkFragment;
import de.tschinder.camlog.fragments.TabListener;
import de.tschinder.camlog.io.ImageStore;
import de.tschinder.camlog.prozess.New;

public class MainActivity extends FragmentActivity
{

    public static final String APP_TAG = "CamLog";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private Uri fileUri;
    private static New newObject;

    private ArrayList<TabListener<? extends ListFragment>> tabListener = new ArrayList<TabListener<? extends ListFragment>>();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initTabs();
    }    
    
    public void initTabs()
    {
        tabListener.clear();
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        TabListener<ListAllFragment> tabListener1 = new TabListener<ListAllFragment>(this, ListAllFragment.class);
        Tab tab1 = actionBar.newTab().setText(R.string.tab_title_all).setTabListener(tabListener1);
        actionBar.addTab(tab1);
        tabListener.add(tabListener1);

        TabListener<ListPrivateFragment> tabListener2 = new TabListener<ListPrivateFragment>(this,
                ListPrivateFragment.class);
        Tab tab2 = actionBar.newTab().setText(R.string.tab_title_private).setTabListener(tabListener2);
        actionBar.addTab(tab2);
        tabListener.add(tabListener2);

        TabListener<ListBusinessFragment> tabListener3 = new TabListener<ListBusinessFragment>(this,
                ListBusinessFragment.class);
        Tab tab3 = actionBar.newTab().setText(R.string.tab_title_business).setTabListener(tabListener3);
        actionBar.addTab(tab3);
        tabListener.add(tabListener3);

        TabListener<ListWorkFragment> tabListener4 = new TabListener<ListWorkFragment>(this, ListWorkFragment.class);
        Tab tab4 = actionBar.newTab().setText(R.string.tab_title_work).setTabListener(tabListener4);
        actionBar.addTab(tab4);
        tabListener.add(tabListener4);
    }

    public void refreshTabs()
    {
        for (TabListener<? extends ListFragment> tabListener : this.tabListener) {
            tabListener.refreshTab();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // // app icon in action bar clicked; go home
                // Intent intent = new Intent(this, MainActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intent);
                // return true;
            case R.id.menu_new:
                // app icon in action bar clicked; go home
                startImageCapture();
                return true;
            case R.id.menu_settings:
                // app icon in action bar clicked; go home
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startImageCapture()
    {
        // create Intent to take a picture and return control to the calling
        // application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = ImageStore.getOutputMediaFileUri(); // create a file to save
                                                      // the image
        Log.v("CamLog", fileUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Uri file;
                if (data != null) {
                    file = data.getData();
                } else {
                    file = fileUri;
                }
                newObject = new New();
                newObject.start(file);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // no-op
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshTabs();
        if (newObject != null) {
            newObject.setActivity(this);
        }
    }

    @Override
    protected void onPause()
    {
        if (newObject != null) {
            newObject.setActivity(null);
        }
        Helper.getInstance(getApplicationContext()).close();
        super.onPause();
    }
}
