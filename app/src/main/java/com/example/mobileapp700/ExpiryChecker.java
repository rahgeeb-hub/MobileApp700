package com.example.mobileapp700;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpiryChecker {

    DatabaseHelper databaseHelper;

    public ExpiryChecker(Context context) {

        // This connects to the database
        databaseHelper = new DatabaseHelper(context);
    }

    // This checks for pantry items expiring within 3 days
    public String checkExpiringItems() {

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingredient, expiry_date FROM pantry",
                null
        );

        String message = "";

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        format.setLenient(false);

        // This gets today's date
        Calendar today = Calendar.getInstance();

        today.set(
                Calendar.HOUR_OF_DAY,
                0
        );

        today.set(
                Calendar.MINUTE,
                0
        );

        today.set(
                Calendar.SECOND,
                0
        );

        today.set(
                Calendar.MILLISECOND,
                0
        );

        // This gets the date three days from today
        Calendar threeDaysLater =
                (Calendar) today.clone();

        threeDaysLater.add(
                Calendar.DAY_OF_YEAR,
                3
        );

        while (cursor.moveToNext()) {

            String ingredient =
                    cursor.getString(0);

            String expiryDate =
                    cursor.getString(1);

            // This ignores items without an expiry date
            if (expiryDate != null &&
                    !expiryDate.isEmpty()) {

                try {

                    Date expiry =
                            format.parse(expiryDate);

                    // This checks if the item expires within 3 days
                    if (!expiry.before(today.getTime()) &&
                            !expiry.after(threeDaysLater.getTime())) {

                        message = message +
                                ingredient +
                                " expires soon on " +
                                expiryDate +
                                "\n";
                    }

                } catch (Exception e) {

                    // Invalid dates are ignored
                }
            }
        }

        cursor.close();

        return message;
    }
}