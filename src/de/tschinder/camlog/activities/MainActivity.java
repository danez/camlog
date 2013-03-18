package de.tschinder.camlog.activities;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.data.LogEntryAdapter;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.Helper;
import de.tschinder.camlog.database.dao.LogEntryDataSource;
import de.tschinder.camlog.database.object.LogEntry;
import de.tschinder.camlog.fragments.ListEntriesFragment;
import de.tschinder.camlog.io.ImageStore;
import de.tschinder.camlog.prozess.New;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener
{

    public static final String EVENT_REFRESH_LIST = "event_refresh_list";
    public static final String APP_TAG = "CamLog";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private Uri fileUri;
    private static New newObject;
    
    private ListFragment mFragment;
    
    protected static int currentFilterPosition = -1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.action_bar_types, android.R.layout.simple_spinner_dropdown_item);        
        actionBar.setListNavigationCallbacks(adapter, this);
        
        if(currentFilterPosition > 0) {
            actionBar.setSelectedNavigationItem(currentFilterPosition);
        }
        
        initFragment();
        
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(EVENT_REFRESH_LIST));
    }
    
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          refreshList();
        }
      };
    
    protected void initFragment()
    {
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction xaction = fragMgr.beginTransaction();

        mFragment = (ListFragment) Fragment.instantiate(this, ListEntriesFragment.class.getName());
        xaction.replace(android.R.id.content, mFragment, ListEntriesFragment.class.getName());
        xaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        currentFilterPosition = itemPosition;
        if (mFragment != null) {
            LogEntryAdapter adapter = (LogEntryAdapter) mFragment.getListAdapter();
            adapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }
    
    public List<LogEntry> getEntriesForCurrentSelection()
    {
        final LogEntryDataSource datasource = new LogEntryDataSource(this);
        switch (currentFilterPosition) {
            case 1: // private
                return datasource.getAllLogEntriesForType(LogEntryType.PRIVATE);
            case 2: // business
                return datasource.getAllLogEntriesForType(LogEntryType.BUSINESS);
            case 3: // work
                return datasource.getAllLogEntriesForType(LogEntryType.WORK);
            default:
                return datasource.getAllLogEntries();
        }
    }

    private void refreshList()
    {
        if (mFragment != null) {
            LogEntryAdapter adapter = (LogEntryAdapter) mFragment.getListAdapter();
            adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
