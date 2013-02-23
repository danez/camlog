package de.tschinder.camlog.fragments;

import java.util.List;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import de.tschinder.camlog.data.LogEntryAdapter;
import de.tschinder.camlog.data.LogEntryType;
import de.tschinder.camlog.database.dao.LogEntryDataSource;
import de.tschinder.camlog.database.object.LogEntry;

public class ListWorkFragment extends ListFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final LogEntryDataSource datasource = new LogEntryDataSource(getActivity());
        List<LogEntry> entries = datasource.getAllLogEntriesForType(LogEntryType.WORK);

        final LogEntryAdapter adapter = new LogEntryAdapter(getActivity(), android.R.layout.simple_list_item_1, entries);
        adapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged()
            {
                List<LogEntry> entries = datasource.getAllLogEntriesForType(LogEntryType.WORK);
                adapter.setItems(entries);
            }

            public void onInvalidated()
            {
                List<LogEntry> entries = datasource.getAllLogEntriesForType(LogEntryType.WORK);
                adapter.setItems(entries);
            }
        });
        this.setListAdapter(adapter);
    }
}
