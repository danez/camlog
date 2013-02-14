package de.tschinder.camlog.database.object;

import android.content.Context;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.MessageDataSource;

public class Message extends Abstract
{
    private LogEntryType type;
    private String value;
    private int count;

    public Message(Context context)
    {
        super(context);
    }

    public LogEntryType getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = LogEntryType.byOrdinal(type);
    }

    public void setType(LogEntryType type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
    
    /**
     * 
     * @return the actual count after increment
     */
    public int incrementCount()
    {
        return ++count;
    }

    @Override
    public String toString()
    {
        return value + " (" + count + ")";
    }

    public void save()
    {
        MessageDataSource mds = new MessageDataSource(context);
        mds.save(this);
    }
}
