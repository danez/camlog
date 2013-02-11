package de.tschinder.camlog.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import de.tschinder.camlog.R;
import de.tschinder.camlog.io.ImageStore;
import de.tschinder.camlog.prozess.New;

public class StartFragment extends Fragment
{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private Uri fileUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        final Button button = (Button) view.findViewById(R.id.capture_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startImageCapture();
            }
        });

        return view;
    }

    public void startImageCapture()
    {

        // create Intent to take a picture and return control to the calling
        // application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = ImageStore.getOutputMediaFileUri(); // create a file to save the image
        Log.v("CamLog", fileUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.v("CamLog", "result");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Uri file;
                if (data != null) {
                    file = data.getData();
                } else {
                    file = fileUri;
                }
                new New(getActivity()).start(file);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // no-op
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }    

}
