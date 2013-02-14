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
import de.tschinder.camlog.database.object.Message;
import de.tschinder.camlog.database.table.MessageTable;

public class MessageDataSource
{

    private Helper dbHelper;
    private Context context;
    private String[] allColumns = {
            MessageTable.COLUMN_ID, MessageTable.COLUMN_TYPE, MessageTable.COLUMN_VALUE, MessageTable.COLUMN_COUNT
    };

    public MessageDataSource(Context context)
    {
        dbHelper = Helper.getInstance(context);
        this.context = context;
    }

    public void close()
    {
        dbHelper.close();
    }

    public synchronized Message createMessage(String value, LogEntryType type)
    {
        
        ContentValues values = new ContentValues();
        values.put(MessageTable.COLUMN_TYPE, type.ordinal());
        values.put(MessageTable.COLUMN_VALUE, value);
        values.put(MessageTable.COLUMN_COUNT, 1);
        
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long insertId = database.insert(MessageTable.TABLE_NAME, null, values);
        
        return findById(insertId);
    }

    public Message findOrCreateMessage(String value, LogEntryType type)
    {
        Message message = findByValueAndType(value, type);
        if (message == null) {
            message = createMessage(value, type);
        }
        return message;
    }

    public Message findByValueAndType(String value, LogEntryType type)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MessageTable.TABLE_NAME, allColumns, MessageTable.COLUMN_VALUE + " = " + value
                + "AND " + MessageTable.COLUMN_TYPE + "=" + type.ordinal(), null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Message message = cursorToComment(cursor);
            
            return message;
        }
        return null;
    }

    public Message findById(long id)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MessageTable.TABLE_NAME, allColumns, MessageTable.COLUMN_ID + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        Message message = cursorToComment(cursor);
        cursor.close();
        
        return message;
    }

    public synchronized boolean deleteMessage(Message message)
    {
        long id = message.getId();
        Log.i(MainActivity.APP_TAG, "Delete message with id: " + id);
        
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int affectedRows = database.delete(MessageTable.TABLE_NAME, MessageTable.COLUMN_ID + " = " + id, null);
        
        
        return affectedRows == 1;
    }

    public List<Message> getAllMessages()
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MessageTable.TABLE_NAME, allColumns, null, null, null, null, null);
        List<Message> messages = cursorToList(cursor);
        
        return messages;
    }

    public List<Message> getAllMessagesByTypeOrderByCount(LogEntryType type)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MessageTable.TABLE_NAME, allColumns,
                MessageTable.COLUMN_TYPE + "=" + type.ordinal(), null, null, null, MessageTable.COLUMN_COUNT
                        + " DESC");
        List<Message> messages = cursorToList(cursor);
        
        return messages;
    }

    private List<Message> cursorToList(Cursor cursor)
    {
        List<Message> messages = new ArrayList<Message>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToComment(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return messages;
    }

    private Message cursorToComment(Cursor cursor)
    {
        Message message = new Message(context);
        message.setId(cursor.getInt(0));
        message.setType(cursor.getInt(1));
        message.setValue(cursor.getString(2));
        message.setCount(cursor.getInt(3));

        return message;
    }

    public synchronized void save(Message message)
    {
        ContentValues values = new ContentValues();

        values.put(MessageTable.COLUMN_TYPE, message.getType().ordinal());
        values.put(MessageTable.COLUMN_VALUE, message.getValue());
        values.put(MessageTable.COLUMN_COUNT, message.getCount());
        
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (message.getId() != 0) {
            database.update(MessageTable.TABLE_NAME, values, MessageTable.COLUMN_ID + " = " + message.getId(), null);
        } else {
            long newId = database.insert(MessageTable.TABLE_NAME, null, values);
            message.setId(newId);
        }
        
    }
}
