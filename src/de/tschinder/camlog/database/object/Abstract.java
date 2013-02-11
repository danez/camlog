package de.tschinder.camlog.database.object;

import android.content.Context;

public class Abstract
{
    private int id;
    protected Context context;

    public Abstract(Context context)
    {
        this.context = context;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
