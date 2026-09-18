package com.example.mobileapp700;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PantryAdapter extends ArrayAdapter<String> {

    public PantryAdapter(Context context, ArrayList<String> items) {
        super(context, android.R.layout.simple_list_item_1, items);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // This gets the normal list row
        TextView row = (TextView) super.getView(position, view, parent);

        // This displays the pantry item
        row.setText(getItem(position));

        return row;
    }
}