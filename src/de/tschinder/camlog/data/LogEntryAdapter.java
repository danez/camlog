package de.tschinder.camlog.data;

import java.io.FileInputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.database.object.LogEntry;

public class LogEntryAdapter extends ArrayAdapter<LogEntry>
{

    private List<LogEntry> items;
    private Context context;

    public LogEntryAdapter(Context context, int textViewResourceId, List<LogEntry> items)
    {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
    }
    
    /**
     * @param items
     */
    public void setItems(List<LogEntry> items)
    {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.history_list_item, null);
        }
        LogEntry logEntry = items.get(position);
        if (logEntry != null) {
            TextView tt = (TextView) v.findViewById(R.id.history_item_description);
            TextView bt = (TextView) v.findViewById(R.id.history_item_date);
            //ImageView image = (ImageView) v.findViewById(R.id.history_item_image);
            if (tt != null) {
                tt.setText(logEntry.getMessage().getValue());
            }
            if (bt != null) {
                bt.setText(logEntry.getDate());
            }
            //if (image != null) {
            //    image.setImageBitmap(getThumbnail(logEntry.getImage()));
           // }
        }
        return v;
    }
    
    

    private Bitmap getThumbnail(String fileName)
    {
        try     
        {

            final int THUMBNAIL_SIZE = 128;

            FileInputStream fis = new FileInputStream(Uri.parse(fileName).getPath());
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            return Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
        }
        catch(Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

}