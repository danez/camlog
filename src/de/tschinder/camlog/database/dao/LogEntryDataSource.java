package de.tschinder.camlog.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.Helper;
import de.tschinder.camlog.database.object.LogEntry;
import de.tschinder.camlog.database.table.LogEntryTable;

public class LogEntryDataSource
{

    private Helper dbHelper;
    private Context context;
    private MessageDataSource messageDataSource;
    private String[] allColumns = {
            LogEntryTable.COLUMN_ID,
            LogEntryTable.COLUMN_TYPE,
            LogEntryTable.COLUMN_MESSAGE,
            LogEntryTable.COLUMN_DATE,
            LogEntryTable.COLUMN_IMAGE
    };

    public LogEntryDataSource(Context context)
    {
        dbHelper = Helper.getInstance(context);
        messageDataSource = new MessageDataSource(context);
        this.context = context;
    }

    public void close()
    {
        dbHelper.close();
    }

    public synchronized LogEntry createLogEntry(String message, LogEntryType type, String image)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(LogEntryTable.COLUMN_TYPE, type.ordinal());
        values.put(LogEntryTable.COLUMN_MESSAGE, messageDataSource.findOrCreateMessage(message, type).getId());
        values.put(LogEntryTable.COLUMN_IMAGE, image);
        long insertId = database.insert(LogEntryTable.TABLE_NAME, null, values);

        database.setTransactionSuccessful();
        database.endTransaction();
        return findById(insertId);
    }

    // public LogEntry findOrCreateMessage(String message, LogEntryType type,
    // String image)
    // {
    // LogEntry logEntry = findByValueAndType(message, type, image);
    // if (logEntry == null) {
    // logEntry = createLogEntry(message, type, image);
    // }
    // return message;
    // }

    // public Message findByValueAndType(String value, LogEntryType type)
    // {
    // Cursor cursor = database.query(MessageHelper.TABLE_NAME, allColumns,
    // MessageHelper.COLUMN_VALUE + " = " + value + "AND "
    // + MessageHelper.COLUMN_TYPE + "=" + type.ordinal(),
    // null, null, null, null);
    // if (cursor.getCount() > 0) {
    // cursor.moveToFirst();
    // Message message = cursorToComment(cursor);
    // cursor.close();
    // return message;
    // }
    // return null;
    // }

    public LogEntry findById(long id)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(LogEntryTable.TABLE_NAME, allColumns, LogEntryTable.COLUMN_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        LogEntry logEntry = cursorToComment(cursor);
        cursor.close();
        return logEntry;
    }

    public synchronized void deleteComment(LogEntry logEntry)
    {
        long id = logEntry.getId();
        Log.i(MainActivity.APP_TAG, "Delete logEntry with id: " + id);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(LogEntryTable.TABLE_NAME, LogEntryTable.COLUMN_ID + " = " + id, null);
    }

    public List<LogEntry> getAllLogEntries()
    {
        List<LogEntry> logEntries = new ArrayList<LogEntry>();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(LogEntryTable.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LogEntry logEntry = cursorToComment(cursor);
            logEntries.add(logEntry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return logEntries;
    }

    private LogEntry cursorToComment(Cursor cursor)
    {
        LogEntry logEntry = new LogEntry(context);
        logEntry.setId(cursor.getInt(0));
        logEntry.setType(cursor.getInt(1));
        logEntry.setMessage(messageDataSource.findById(cursor.getInt(2)));
        logEntry.setDate(cursor.getString(3));
        logEntry.setImage(cursor.getString(4));

        return logEntry;
    }

    public synchronized void save(LogEntry logEntry)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            logEntry.getMessage().save();

            ContentValues values = new ContentValues();
            values.put(LogEntryTable.COLUMN_TYPE, logEntry.getType().ordinal());
            values.put(LogEntryTable.COLUMN_MESSAGE, logEntry.getMessage().getId());
            values.put(LogEntryTable.COLUMN_IMAGE, logEntry.getImage());
            if (logEntry.getId() != 0) {
                database.update(LogEntryTable.TABLE_NAME, values, LogEntryTable.COLUMN_ID + " = " + logEntry.getId(),
                        null);
            } else {
                long newId = database.insert(LogEntryTable.TABLE_NAME, null, values);
                logEntry.setId(newId);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
