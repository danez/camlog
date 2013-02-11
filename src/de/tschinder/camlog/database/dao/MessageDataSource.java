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
import de.tschinder.camlog.database.helper.MessageHelper;
import de.tschinder.camlog.database.object.Message;

public class MessageDataSource
{

    // Database fields
    private SQLiteDatabase database;
    private MessageHelper dbHelper;
    private Context context;
    private boolean isOpen = false;
    private String[] allColumns = {
            MessageHelper.COLUMN_ID, MessageHelper.COLUMN_TYPE, MessageHelper.COLUMN_VALUE, MessageHelper.COLUMN_COUNT
    };

    public MessageDataSource(Context context)
    {
        dbHelper = new MessageHelper(context);
        this.context = context;
    }

    public synchronized void open() throws SQLException
    {
        if (!isOpen) {
            database = dbHelper.getWritableDatabase();
            isOpen = true;
        }
    }

    public void close()
    {
        dbHelper.close();
    }

    public Message createMessage(String value, LogEntryType type)
    {
        open();
        ContentValues values = new ContentValues();
        values.put(MessageHelper.COLUMN_TYPE, type.ordinal());
        values.put(MessageHelper.COLUMN_VALUE, value);
        values.put(MessageHelper.COLUMN_COUNT, 1);
        long insertId = database.insert(MessageHelper.TABLE_NAME, null, values);

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
        open();
        Cursor cursor = database.query(MessageHelper.TABLE_NAME, allColumns, MessageHelper.COLUMN_VALUE + " = " + value
                + "AND " + MessageHelper.COLUMN_TYPE + "=" + type.ordinal(), null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Message message = cursorToComment(cursor);
            cursor.close();
            return message;
        }
        return null;
    }

    public Message findById(long id)
    {
        open();
        Cursor cursor = database.query(MessageHelper.TABLE_NAME, allColumns, MessageHelper.COLUMN_ID + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        Message message = cursorToComment(cursor);
        cursor.close();
        return message;
    }

    public boolean deleteMessage(Message message)
    {
        open();
        long id = message.getId();
        Log.i(MainActivity.APP_TAG, "Delete message with id: " + id);
        int affectedRows = database.delete(MessageHelper.TABLE_NAME, MessageHelper.COLUMN_ID + " = " + id, null);
        return affectedRows == 1;
    }

    public List<Message> getAllMessages()
    {
        open();
        Cursor cursor = database.query(MessageHelper.TABLE_NAME, allColumns, null, null, null, null, null);

        return cursorToList(cursor);
    }

    public List<Message> getAllMessagesByTypeOrderByCount(LogEntryType type)
    {
        open();
        Cursor cursor = database.query(MessageHelper.TABLE_NAME, allColumns,
                MessageHelper.COLUMN_TYPE + "=" + type.ordinal(), null, null, null, MessageHelper.COLUMN_COUNT
                        + " DESC");

        return cursorToList(cursor);
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

    public void save(Message message)
    {
        open();
        ContentValues values = new ContentValues();

        values.put(MessageHelper.COLUMN_TYPE, message.getType().ordinal());
        values.put(MessageHelper.COLUMN_VALUE, message.getValue());
        values.put(MessageHelper.COLUMN_COUNT, message.getCount());
        if (message.getId() != 0) {
            database.update(MessageHelper.TABLE_NAME, values, MessageHelper.COLUMN_ID + " = " + message.getId(), null);
        } else {
            database.insert(MessageHelper.TABLE_NAME, null, values);
        }

    }
}
