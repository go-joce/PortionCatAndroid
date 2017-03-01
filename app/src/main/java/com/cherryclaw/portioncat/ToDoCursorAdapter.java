package com.cherryclaw.portioncat;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.cherryclaw.portioncat.DbBitmapUtility.getImage;

/**
 * ToDoCursorAdapter binds views for the ListView and inflates layout cat_list_item.
 *
 * @author Jocelynne Gonzales
 */

public class ToDoCursorAdapter extends CursorAdapter {
    public ToDoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cat_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template

        TextView tvName = (TextView) view.findViewById(R.id.cat_name);
        TextView tvFoodType = (TextView) view.findViewById(R.id.cat_food_type);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String foodType = cursor.getString(cursor.getColumnIndexOrThrow("foodType"));
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("catImage"));


        tvFoodType.setText(foodType);
        Log.d(null, "name: "+name);
        tvName.setText(name);

        /* Set cat image as a thumbnail in the list view */
        ImageView catPhoto = (ImageView) view.findViewById(R.id.imageView2);
        catPhoto.setImageBitmap(getImage(image));
    }



}