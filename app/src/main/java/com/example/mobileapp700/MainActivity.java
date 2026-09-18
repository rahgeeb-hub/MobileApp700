package com.example.mobileapp700;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the main screen
        setContentView(R.layout.activity_main);

        // This adds the starting recipes
        RecipeDatabase recipeDatabase = new RecipeDatabase(this);
        recipeDatabase.seedRecipes();

        // This gets the saved settings
        SharedPreferences preferences =
                getSharedPreferences("Settings", MODE_PRIVATE);

        boolean expiryAlerts =
                preferences.getBoolean("expiryAlerts", false);

        // This checks for expiring pantry items
        if (expiryAlerts) {

            ExpiryChecker expiryChecker = new ExpiryChecker(this);
            String message = expiryChecker.checkExpiringItems();

            // This displays the expiry alert
            if (!message.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Expiry Alert")
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .show();
            }
        }

        // This gets the buttons
        Button btnPantry = findViewById(R.id.btnPantry);
        Button btnRecipes = findViewById(R.id.btnRecipes);
        Button btnSettings = findViewById(R.id.btnSettings);

        // This button opens the pantry screen
        btnPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        MainActivity.this,
                        PantryActivity.class
                );

                startActivity(intent);
            }
        });

        // This button opens the suggested recipes screen
        btnRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SuggestedRecipesActivity.class
                );

                startActivity(intent);
            }
        });

        // This button opens the settings screen
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
            }
        });
    }

    // This displays the navigation menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    // This opens the selected screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuPantry) {
            startActivity(
                    new Intent(this, PantryActivity.class)
            );
            return true;
        }

        if (id == R.id.menuRecipes) {
            startActivity(
                    new Intent(this, SuggestedRecipesActivity.class)
            );
            return true;
        }

        if (id == R.id.menuSettings) {
            startActivity(
                    new Intent(this, SettingsActivity.class)
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}