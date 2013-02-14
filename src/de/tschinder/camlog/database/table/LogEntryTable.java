package de.tschinder.camlog.database.table;

import de.tschinder.camlog.interfaces.TableInterface;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class LogEntryTable implements TableInterface
{

    public static final String TABLE_NAME = "log_entry";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE = "image";

    @Override
    public synchronized void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TYPE + " INTEGER NOT NULL, " + COLUMN_MESSAGE
                + " INTEGER NOT NULL, " + COLUMN_DATE + " TEXT(19) NOT NULL DEFAULT CURRENT_TIMESTAMP, " + COLUMN_IMAGE
                + " TEXT NOT NULL);");
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
