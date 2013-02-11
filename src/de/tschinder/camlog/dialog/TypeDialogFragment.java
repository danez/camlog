package de.tschinder.camlog.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import de.tschinder.camlog.R;

public class TypeDialogFragment extends DialogFragment
{
    public interface TypeDialogListener
    {
        public void onDialogClick(DialogInterface dialog, int which);
    }

    protected static TypeDialogListener listener;

    public TypeDialogFragment()
    {
        super();
    }
    
    public static TypeDialogFragment show(FragmentActivity activity, TypeDialogListener typeDialogListener)
    {
        listener = typeDialogListener;
        
        TypeDialogFragment fragmentDialog = new TypeDialogFragment();        
        fragmentDialog.show(activity.getSupportFragmentManager(), "camlog_type_dialog");
        
        //activity.getSupportFragmentManager().executePendingTransactions();

        return fragmentDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_choose_type);
        builder.setItems(R.array.entry_types, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                listener.onDialogClick(dialog, which);
            }
        });
        return builder.create();
    }
}
