package com.cherryclaw.portioncat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


/**
 * DbBitmapUtility is used to convert images from bitmap to byte array
 * and vice versa.
 * <p>
 * While taking and storing photos in {@link com.cherryclaw.portioncat.AddCat#onActivityResult(int, int, Intent)},
 * these methods are needed to display the images as thumbnails in bitmap
 * form and store them in the database as byte arrays.
 *
 * @author      Jocelynne Gonzales
 */


public class DbBitmapUtility {
    public DbBitmapUtility() {

    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}