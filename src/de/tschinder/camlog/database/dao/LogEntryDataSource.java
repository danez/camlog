package de.tschinder.camlog.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.helper.LogEntryHelper;
import de.tschinder.camlog.database.object.LogEntry;

public class LogEntryDataSource
{

    // Database fields
    private SQLiteDatabase database;
    private LogEntryHelper dbHelper;
    private Context context;
    private MessageDataSource messageDataSource;
    private String[] allColumns = {
            LogEntryHelper.COLUMN_ID,
            LogEntryHelper.COLUMN_TYPE,
            LogEntryHelper.COLUMN_MESSAGE,
            LogEntryHelper.COLUMN_DATE,
            LogEntryHelper.COLUMN_IMAGE
    };

    public LogEntryDataSource(Context context)
    {
        dbHelper = new LogEntryHelper(context);
        messageDataSource = new MessageDataSource(context);
        this.context = context;
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public LogEntry createLogEntry(String message, LogEntryType type, String image)
    {
        database.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(LogEntryHelper.COLUMN_TYPE, type.ordinal());
        values.put(LogEntryHelper.COLUMN_MESSAGE, messageDataSource.findOrCreateMessage(message, type).getId());
        values.put(LogEntryHelper.COLUMN_IMAGE, image);
        long insertId = database.insert(LogEntryHelper.TABLE_NAME, null, values);

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
        Cursor cursor = database.query(LogEntryHelper.TABLE_NAME, allColumns, LogEntryHelper.COLUMN_ID + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        LogEntry logEntry = cursorToComment(cursor);
        cursor.close();
        return logEntry;
    }

    public void deleteComment(LogEntry logEntry)
    {
        long id = logEntry.getId();
        Log.i(MainActivity.APP_TAG, "Delete logEntry with id: " + id);
        database.delete(LogEntryHelper.TABLE_NAME, LogEntryHelper.COLUMN_ID + " = " + id, null);
    }

    public List<LogEntry> getAllLogEntries()
    {
        List<LogEntry> logEntries = new ArrayList<LogEntry>();

        Cursor cursor = database.query(LogEntryHelper.TABLE_NAME, allColumns, null, null, null, null, null);

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

    public void save(LogEntry logEntry)
    {
        database.beginTransaction();
        logEntry.getMessage().save();

        ContentValues values = new ContentValues();
        values.put(LogEntryHelper.COLUMN_TYPE, logEntry.getType().ordinal());
        values.put(LogEntryHelper.COLUMN_MESSAGE, logEntry.getMessage().getId());
        values.put(LogEntryHelper.COLUMN_IMAGE, logEntry.getImage());
        if (logEntry.getId() != 0) {
            database.update(LogEntryHelper.TABLE_NAME, values, LogEntryHelper.COLUMN_ID + " = " + logEntry.getId(),
                    null);
        } else {
            database.insert(LogEntryHelper.TABLE_NAME, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
