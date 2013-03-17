package de.tschinder.camlog.fragments;

import java.util.List;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import de.tschinder.camlog.activities.MainActivity;
import de.tschinder.camlog.data.LogEntryAdapter;
import de.tschinder.camlog.database.object.LogEntry;

public class ListEntriesFragment extends ListFragment 
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        List<LogEntry> entries = ((MainActivity)getActivity()).getEntriesForCurrentSelection();

        final LogEntryAdapter adapter = new LogEntryAdapter(this.getActivity(), android.R.layout.simple_list_item_1, entries);
        adapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged()
            {
                List<LogEntry> entries = ((MainActivity)getActivity()).getEntriesForCurrentSelection();
                adapter.setItems(entries);
            }

            public void onInvalidated()
            {
                List<LogEntry> entries = ((MainActivity)getActivity()).getEntriesForCurrentSelection();
                adapter.setItems(entries);
            }
        });
        this.setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        LogEntry logEntry = (LogEntry)getListAdapter().getItem(position);
        
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(logEntry.getImage()), "image/jpeg");
        startActivity(i);
    }
}
