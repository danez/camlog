package de.tschinder.camlog.database.object;

import android.content.Context;

public class Abstract
{
    private long id;
    protected Context context;

    public Abstract(Context context)
    {
        this.context = context;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
