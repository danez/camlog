package de.tschinder.camlog.core;

import de.tschinder.camlog.database.Helper;

public class Application extends android.app.Application
{

    @Override
    public void onCreate()
    {
        // Update the database
        new Thread(new Runnable() {
            public void run() {
                Helper.getInstance(Application.this).getReadableDatabase().close();
            }
        }).start();
        
        super.onCreate();
        
    } 
}
