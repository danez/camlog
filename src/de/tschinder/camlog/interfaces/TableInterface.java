package de.tschinder.camlog.interfaces;

import android.database.sqlite.SQLiteDatabase;

public interface TableInterface
{
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
