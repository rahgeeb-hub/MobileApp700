package com.example.mobileapp700;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the main screen
        setContentView(R.layout.activity_main);

        // This gets the pantry button
        Button btnPantry = findViewById(R.id.btnPantry);

        // This button opens the pantry screen
        btnPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, PantryActivity.class);
                startActivity(intent);
            }
        });
    }
}