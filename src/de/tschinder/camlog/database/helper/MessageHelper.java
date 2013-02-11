package de.tschinder.camlog.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MessageHelper extends SQLiteOpenHelper
{

    public static final String TABLE_NAME = "message";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_COUNT = "count";

    private static final String DATABASE_NAME = "camlog";
    private static final int TABLE_VERSION = 1;

    public MessageHelper(Context context)
    {
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TYPE + " INTEGER NOT NULL, " + COLUMN_VALUE
                + " TEXT NOT NULL, " + COLUMN_COUNT + " INTEGER NOT NULL DEFAULT 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {

    }

}
