package com.example.mobileapp700;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // This sets the database name
    private static final String DATABASE_NAME = "SmartPantry.db";

    // This sets the database version
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // This creates the pantry table
        db.execSQL("CREATE TABLE pantry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ingredient TEXT, " +
                "quantity REAL, " +
                "unit TEXT, " +
                "expiry_date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This removes the old pantry table
        db.execSQL("DROP TABLE IF EXISTS pantry");

        // This creates the pantry table again
        onCreate(db);
    }

    // This adds an item to the pantry table
    public boolean addPantryItem(
            String ingredient,
            double quantity,
            String unit,
            String expiryDate
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ingredient", ingredient);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        long result = db.insert("pantry", null, values);

        return result != -1;
    }

    // This gets all the pantry items
    public ArrayList<String> getPantryItems() {

        ArrayList<String> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM pantry",
                null
        );

        while (cursor.moveToNext()) {

            String ingredient = cursor.getString(1);
            double quantity = cursor.getDouble(2);
            String unit = cursor.getString(3);
            String expiryDate = cursor.getString(4);

            String item = ingredient + " - " + quantity + " " + unit;

            if (expiryDate != null && !expiryDate.isEmpty()) {
                item = item + " - Expiry: " + expiryDate;
            }

            pantryItems.add(item);
        }

        cursor.close();

        return pantryItems;
    }

    // This gets the ID of each pantry item
    public ArrayList<Integer> getPantryIds() {

        ArrayList<Integer> pantryIds = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM pantry",
                null
        );

        while (cursor.moveToNext()) {
            pantryIds.add(cursor.getInt(0));
        }

        cursor.close();

        return pantryIds;
    }

    // This updates a pantry item
    public boolean updatePantryItem(
            int id,
            String ingredient,
            double quantity,
            String unit,
            String expiryDate
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ingredient", ingredient);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        int result = db.update(
                "pantry",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // This deletes a pantry item
    public boolean deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "pantry",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}