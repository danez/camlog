package de.tschinder.camlog.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.tschinder.camlog.database.table.LogEntryTable;
import de.tschinder.camlog.database.table.MessageTable;
import de.tschinder.camlog.interfaces.TableInterface;

public class Helper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "camlog";

    private static Helper instance;

    private Helper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Helper getInstance(Context context)
    {
        if (instance == null) {
            instance = new Helper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public synchronized void close()
    {
        instance = null;
        super.close();
    }

    protected List<TableInterface> getTableObjects()
    {
        List<TableInterface> tables = new ArrayList<TableInterface>();
        tables.add(new LogEntryTable());
        tables.add(new MessageTable());
        return tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        for (TableInterface table : getTableObjects()) {
            table.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (TableInterface table : getTableObjects()) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
