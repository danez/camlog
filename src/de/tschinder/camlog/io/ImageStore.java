package de.tschinder.camlog.io;

import java.io.File;

import android.net.Uri;

public class ImageStore
{

    protected static final String EXT_IMAGE_DIR = RootStore.EXT_ROOT_DIR + "/images";

    public static Uri getOutputMediaFileUri()
    {
        File file = new File(getImageDir(), System.currentTimeMillis() + ".jpg");
        return Uri.fromFile(file);
    }

    public static String getImageDir()
    {
        createImageDir();
        return EXT_IMAGE_DIR;
    }

    protected static boolean createImageDir()
    {
        return new File(EXT_IMAGE_DIR).mkdirs();
    }
    
    public static boolean deleteImage(String pathToFile)
    {
        return new File(pathToFile).delete();
    }
}
