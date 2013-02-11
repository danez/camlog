package de.tschinder.camlog.database.object;

import android.content.Context;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.LogEntryDataSource;

public class LogEntry extends Abstract
{
    private LogEntryType type;
    private Message message;
    private String date;
    private String image;

    public LogEntry(Context context)
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

    public Message getMessage()
    {
        return message;
    }

    public void setMessage(Message message)
    {
        this.message = message;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return message.toString();
    }

    public void save()
    {
        LogEntryDataSource lds = new LogEntryDataSource(context);
        lds.save(this);
    }
}
