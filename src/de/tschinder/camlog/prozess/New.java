package de.tschinder.camlog.prozess;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.MessageDataSource;
import de.tschinder.camlog.database.object.LogEntry;
import de.tschinder.camlog.database.object.Message;
import de.tschinder.camlog.dialog.MessageDialogFragment;
import de.tschinder.camlog.dialog.NewMessageDialogFragment;
import de.tschinder.camlog.dialog.TypeDialogFragment;
import de.tschinder.camlog.io.ImageStore;

public class New implements TypeDialogFragment.TypeDialogListener, MessageDialogFragment.MessageDialogListener,
        NewMessageDialogFragment.NewMessageDialogListener
{

    private LogEntry logEntry;
    private FragmentActivity context;

    private String dialogQueue;

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
        try {
            Message message = MessageDialogFragment.getMessages().get(which);
            message.incrementCount();
            logEntry.setMessage(message);
            saveLogEntry();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(context, "Something went wrong, bug report?", Toast.LENGTH_LONG).show();
            Log.e(MainActivity.APP_TAG, "Index out of bonund: " + which);
            dialog.cancel();
        }

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
        Message messageObject = new Message(context);
        messageObject.setCount(1);
        messageObject.setType(logEntry.getType());
        messageObject.setValue(message);
        logEntry.setMessage(messageObject);
        saveLogEntry();
    }

    public void saveLogEntry()
    {
        logEntry.save();
        logEntry = null;
        dialogQueue = "";
        Intent intent = new Intent(MainActivity.EVENT_REFRESH_LIST);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onNewMessageDialogAbort(DialogInterface dialog, int id)
    {
        showDialogMessage();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        // remove the image
        ImageStore.deleteImage(logEntry.getImage());
        logEntry = null;
        dialogQueue = "";
    }
}
