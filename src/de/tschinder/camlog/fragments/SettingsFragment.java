package de.tschinder.camlog.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.database.Helper;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        initPreferences();

    }

    // Fragment
    public void initPreferences()
    {
        Preference backup = (Preference) findPreference("pref_key_backup_now");
        backup.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference)
            {
                new Thread(new Runnable() {
                    public void run()
                    {
                        createBackup();
                    }
                }).start();

                return true;
            }
        });

        Preference restore = (Preference) findPreference("pref_key_restore_backup_now");
        restore.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference)
            {
                new Thread(new Runnable() {
                    public void run()
                    {
                        restoreBackup();
                    }
                }).start();
                return true;
            }
        });
    }

    protected void createBackup()
    {
        File target = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camlog/db.backup");
        File source = new File(getActivity().getDatabasePath(Helper.DATABASE_NAME).getAbsolutePath());
        if (target.exists())
            target.delete();

        if (source.exists()) {
            try {
                FileChannel src = new FileInputStream(source).getChannel();
                FileChannel dst = new FileOutputStream(target).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                makeToast(R.string.backup_finished, Toast.LENGTH_LONG);
            } catch (IOException ex) {
                makeToast(R.string.backup_failed, Toast.LENGTH_LONG);
            }
        }
    }
    
    protected void restoreBackup()
    {
        File source = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camlog/db.backup");
        File target = new File(getActivity().getDatabasePath(Helper.DATABASE_NAME).getAbsolutePath());
        if(!source.exists())
        {
            makeToast(R.string.restore_does_not_exist, Toast.LENGTH_LONG);
            return;
        }
        
        
        if (target.exists()) {
            target.renameTo(new File(getActivity().getDatabasePath(Helper.DATABASE_NAME).getAbsolutePath() + ".before-restore"));
        }

        if (source.exists()) {
            try {
                FileChannel src = new FileInputStream(source).getChannel();
                FileChannel dst = new FileOutputStream(target).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                makeToast(R.string.restore_finished, Toast.LENGTH_LONG);
            } catch (IOException ex) {
                makeToast(R.string.restore_failed, Toast.LENGTH_LONG);
            }
        }
    }
    
    protected void makeToast(final int resId, final int duration)
    {
        getActivity().runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(getActivity(), resId, duration).show();
            }
        });
    }

}
