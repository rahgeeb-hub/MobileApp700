package com.example.mobileapp700;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

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

        createRecipeTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This creates the recipe tables for version 2
        if (oldVersion < 2) {
            createRecipeTables(db);
        }

        // This resets only the recipe tables for version 3
        if (oldVersion < 3) {

            db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
            db.execSQL("DROP TABLE IF EXISTS recipes");

            createRecipeTables(db);
        }
    }

    // This creates the recipe tables
    private void createRecipeTables(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "steps TEXT)");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "ingredient TEXT, " +
                "quantity REAL, " +
                "unit TEXT)");
    }

    // This adds an item to the pantry
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

        long result = db.insert(
                "pantry",
                null,
                values
        );

        return result != -1;
    }

    // This gets all pantry items
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

            String item =
                    ingredient + " - " +
                            quantity + " " +
                            unit;

            if (expiryDate != null && !expiryDate.isEmpty()) {

                item = item +
                        " - Expiry: " +
                        expiryDate;
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