package de.tschinder.camlog.dialog;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.database.object.Message;

public class MessageDialogFragment extends DialogFragment
{
    public interface MessageDialogListener
    {
        public void onMessageDialogClick(DialogInterface dialog, int which);

        public void onMessageDialogClickNewMessage(DialogInterface dialog, int id);
    }

    protected static MessageDialogListener listener;
    protected static List<Message> messages;

    public MessageDialogFragment()
    {
        super();
    }

    public static MessageDialogFragment show(FragmentActivity activity, MessageDialogListener listener,
            List<Message> messages)
    {
        MessageDialogFragment.listener = listener;
        MessageDialogFragment.messages = messages;

        MessageDialogFragment fragmentDialog = new MessageDialogFragment();
        fragmentDialog.show(activity.getSupportFragmentManager(), "camlog_message_dialog");

        return fragmentDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(getActivity(), android.R.layout.simple_list_item_1,
                messages);

        builder.setAdapter(adapter, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d(MainActivity.APP_TAG, "yes choosen " + which);
                listener.onMessageDialogClick(dialog, which);

            }
        }).setNeutralButton("create new message", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Log.d(MainActivity.APP_TAG, "new choosen " + id);
                listener.onMessageDialogClickNewMessage(dialog, id);
            }
        });

        return builder.create();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

}
