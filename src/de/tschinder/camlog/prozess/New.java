package de.tschinder.camlog.prozess;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.MessageDataSource;
import de.tschinder.camlog.database.object.LogEntry;
import de.tschinder.camlog.database.object.Message;

public class New
{

    private LogEntry logEntry;
    private Context context;

    public New(Context context)
    {
        this.context = context;
    }

    private void init()
    {
        logEntry = new LogEntry(context);
    }

    public void start(Uri fileUri)
    {
        init();
        logEntry.setImage(fileUri.toString());
        showDialogTypes();

    }

    protected void showDialogTypes()
    {
        Builder builder = new Builder(context);

        builder.setTitle(R.string.dialog_choose_type);
        builder.setItems(R.array.entry_types, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                processResultDialogTypes(dialog, which);
            }
        });
        builder.show();
    }

    protected void processResultDialogTypes(DialogInterface dialog, int which)
    {
        try {
            logEntry.setType(LogEntryType.byOrdinal(which));
            showDialogMessage();

        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "ERROR: Wrong type choosen.", Toast.LENGTH_LONG).show();
        }
    }

    protected void showDialogMessage()
    {
        MessageDataSource dataSource = new MessageDataSource(context);
        List<Message> messages = dataSource.getAllMessagesByTypeOrderByCount(logEntry.getType());
        if (messages.size() > 0) {
            createDialogMessage(messages).show();
        } else {
            createDialogNewMessage(false).show();
        }

    }

    protected Dialog createDialogMessage(List<Message> messages)
    {
        Builder builder = new Builder(context);

        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(context, android.R.layout.simple_list_item_1,
                messages);

        builder.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d(MainActivity.APP_TAG, "yes choosen " + which);

            }
        }).setNeutralButton("create new message", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Log.d(MainActivity.APP_TAG, "new choosen " + id);
                createDialogNewMessage(true);
            }
        });

        return builder.create();
    }

    protected Dialog createDialogNewMessage(boolean abortPossible)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        Builder builder = new Builder(context);
        View view = inflater.inflate(R.layout.dialog_new_message, null);

        builder.setView(view);
        builder.setPositiveButton("create", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d(MainActivity.APP_TAG, "create new choosen " + which);
            }
        });

        if (abortPossible) {
            builder.setNegativeButton("abort", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.d(MainActivity.APP_TAG, "no choosen " + which);
                    dialog.cancel();
                }
            });
        }

        return builder.create();
    }
}
