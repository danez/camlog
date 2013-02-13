package de.tschinder.camlog.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import de.tschinder.camlog.R;
import de.tschinder.camlog.activities.MainActivity;

public class NewMessageDialogFragment extends DialogFragment
{
    public interface NewMessageDialogListener
    {
        public void onNewMessageDialogCreate(DialogInterface dialog, int id, String message);

        public void onNewMessageDialogAbort(DialogInterface dialog, int id);
    }

    protected static NewMessageDialogListener listener;

    public NewMessageDialogFragment()
    {
        super();
    }

    public static NewMessageDialogFragment show(FragmentActivity activity, NewMessageDialogListener listener,
            boolean abortPossible)
    {
        NewMessageDialogFragment.listener = listener;

        Bundle bundle = new Bundle();
        bundle.putBoolean("abort_possible", abortPossible);

        NewMessageDialogFragment fragmentDialog = new NewMessageDialogFragment();
        fragmentDialog.setArguments(bundle);
        fragmentDialog.show(activity.getSupportFragmentManager(), "camlog_new_message_dialog");

        return fragmentDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_new_message, null);

        builder.setView(view);
        builder.setTitle(R.string.dialog_new_message_title);
        final EditText newMessageInput = (EditText) view.findViewById(R.id.editTextNewMessage);
        
        
        builder.setPositiveButton("create", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Log.d(MainActivity.APP_TAG, "create new choosen " + id);
                if(newMessageInput.getText().length() >= 1) {
                    listener.onNewMessageDialogCreate(dialog, id, newMessageInput.getText().toString());
                }
            }
        });

        if (getArguments().getBoolean("abort_possible", false)) {
            builder.setNegativeButton("abort", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    Log.d(MainActivity.APP_TAG, "no choosen " + id);
                    dialog.cancel();
                }
            });
        }
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
