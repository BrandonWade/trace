package com.example.brandon.trace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Brandon on 9/27/2016.
 */

public class FileListItem {
    public String fileName;
    public String status;
    public String icon;

    public FileListItem(String fileName, String status) { //, String icon) {
        this.fileName = fileName;
        this.status = status;
//        this.icon = icon;
    }

    public Bitmap getIcon() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(icon, options);
        int imgHeight = options.outHeight;
        int imgWidth = options.outWidth;
        int inSampleSize = 1;
        int iconHeight = 100;
        int iconWidth = 100;

        // Determine a sample size for scaling down the image (if necessary)
        if (imgHeight > iconHeight || imgWidth > iconWidth) {
            final int halfHeight = imgHeight / 2;
            final int halfWidth = imgWidth / 2;

            while ((halfHeight / inSampleSize) > iconHeight && (halfWidth / inSampleSize) > iconWidth) {
                inSampleSize *= 2;
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(icon, options);
    }
}
