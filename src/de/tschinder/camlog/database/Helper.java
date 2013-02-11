package de.tschinder.camlog.database;

import android.content.Context;
import de.tschinder.camlog.database.helper.LogEntryHelper;
import de.tschinder.camlog.database.helper.MessageHelper;

public class Helper {
    
    /**
     * This just initializes all tables, which check self for updates
     * @param context
     */
    public static void upgradeDatabase(Context context)
    {
	new LogEntryHelper(context);
	new MessageHelper(context);
    }
}
