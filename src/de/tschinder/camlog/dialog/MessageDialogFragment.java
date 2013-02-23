package de.tschinder.camlog.dialog;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import de.tschinder.camlog.database.object.Message;

public class MessageDialogFragment extends DialogFragment
{
    public interface MessageDialogListener
    {
        public void onMessageDialogClick(DialogInterface dialog, int which);

        public void onMessageDialogClickNewMessage(DialogInterface dialog, int id);

        public void onCancel(DialogInterface dialog);
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

    public static List<Message> getMessages()
    {
        return messages;
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
                listener.onMessageDialogClick(dialog, which);

            }
        }).setNeutralButton("create new message", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                listener.onMessageDialogClickNewMessage(dialog, id);
            }
        });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        listener.onCancel(dialog);
    }
}
