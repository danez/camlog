package de.tschinder.camlog.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class LogEntryHelper extends SQLiteOpenHelper
{

    public static final String TABLE_NAME = "log_entry";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE = "image";

    private static final String DATABASE_NAME = "camlog";
    private static final int TABLE_VERSION = 1;

    public LogEntryHelper(Context context)
    {
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TYPE + " INTEGER NOT NULL, " + COLUMN_MESSAGE
                + " INTEGER NOT NULL, " + COLUMN_DATE + " TEXT(19) NOT NULL DEFAULT CURRENT_TIMESTAMP, " + COLUMN_IMAGE
                + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {

    }

}
