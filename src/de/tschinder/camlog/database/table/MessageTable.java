package de.tschinder.camlog.database.table;

import de.tschinder.camlog.interfaces.TableInterface;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class MessageTable implements TableInterface
{

    public static final String TABLE_NAME = "message";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_COUNT = "count";

    @Override
    public synchronized void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TYPE + " INTEGER NOT NULL, " + COLUMN_VALUE
                + " TEXT NOT NULL, " + COLUMN_COUNT + " INTEGER NOT NULL DEFAULT 1);");
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
