package de.tschinder.camlog.prozess;

import java.util.List;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.MessageDataSource;
import de.tschinder.camlog.database.object.LogEntry;
import de.tschinder.camlog.database.object.Message;
import de.tschinder.camlog.dialog.MessageDialogFragment;
import de.tschinder.camlog.dialog.NewMessageDialogFragment;
import de.tschinder.camlog.dialog.TypeDialogFragment;

public class New implements TypeDialogFragment.TypeDialogListener, MessageDialogFragment.MessageDialogListener,
        NewMessageDialogFragment.NewMessageDialogListener
{

    private LogEntry logEntry;
    private FragmentActivity context;

    private String dialogQueue;

    public New(FragmentActivity context)
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

    public void setActivity(FragmentActivity context)
    {
        this.context = context;
        resumeDialog();
    }

    private void resumeDialog()
    {
        if (dialogQueue == "type") {
            showDialogTypes();
        } else if (dialogQueue == "message") {
            showDialogMessage();
        } else if (dialogQueue == "newtrue") {
            showDialogNewMessage(true);
        } else if (dialogQueue == "newfalse") {
            showDialogNewMessage(false);
        }

    }

    protected void showDialogTypes()
    {
        if (context == null) {
            dialogQueue = "type";
        } else {
            TypeDialogFragment.show(context, this);
        }
    }

    @Override
    public void onTypeDialogClick(DialogInterface dialog, int which)
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
        if (context == null) {
            dialogQueue = "message";
        } else {
            MessageDataSource dataSource = new MessageDataSource(context);
            List<Message> messages = dataSource.getAllMessagesByTypeOrderByCount(logEntry.getType());
            if (messages.size() > 0) {
                MessageDialogFragment.show(context, this, messages);
            } else {
                showDialogNewMessage(false);
            }
        }

    }

    @Override
    public void onMessageDialogClick(DialogInterface dialog, int which)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMessageDialogClickNewMessage(DialogInterface dialog, int id)
    {
        showDialogNewMessage(true);
    }

    protected void showDialogNewMessage(boolean abortPossible)
    {
        if (context == null) {
            dialogQueue = "new" + abortPossible;
        } else {
            NewMessageDialogFragment.show(context, this, abortPossible);
        }

    }

    @Override
    public void onNewMessageDialogCreate(DialogInterface dialog, int id, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNewMessageDialogAbort(DialogInterface dialog, int id)
    {
        // TODO Auto-generated method stub
    }

}
