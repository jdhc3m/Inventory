package com.example.jd158.inventory;

/**
 * Created by jd158 on 7/11/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jd158.inventory.data.InventoryContract;

/**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class InventoryCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // TODO: Fill out this method

        final TextView nameView = (TextView) view.findViewById(R.id.name);
        final TextView tvQuantity = (TextView) view.findViewById(R.id.quantity);
        final TextView tvPrice = (TextView) view.findViewById(R.id.price);

        // Extract form the cursor
        final String itemId = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE));
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.image);

        //Populate Fields with the properties
        nameView.setText(name);
        tvPrice.setText("Price: $"+price);
        tvQuantity.setText("Quantity: " + quantity);

        Button sellButton = (Button) view.findViewById(R.id.button_sell);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY, quantity - 1);
                    String[] selectionArgs = {String.valueOf(itemId)};
                    String selection = " _id = ?";
                    context.getContentResolver().update(InventoryContract.InventoryEntry.CONTENT_URI, values, selection,
                            selectionArgs);
                    tvQuantity.setText("Quantity: " + quantity);
                }

                else {
                    Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_LONG).show();
                    tvQuantity.setText("0");
                }
            }

        });
    }


}