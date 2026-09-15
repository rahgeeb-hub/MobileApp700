package com.example.mobileapp700;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView txtRecipeName;
    TextView txtIngredients;
    TextView txtSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the recipe detail screen
        setContentView(R.layout.activity_recipe_detail);

        // This gets the text fields
        txtRecipeName = findViewById(R.id.txtRecipeName);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtSteps = findViewById(R.id.txtSteps);

        // This gets the recipe name from the previous screen
        String recipeName =
                getIntent().getStringExtra("recipeName");

        // This connects to the recipe database
        RecipeDatabase recipeDatabase =
                new RecipeDatabase(this);

        // This gets the recipe information
        String ingredients =
                recipeDatabase.getRecipeIngredients(recipeName);

        String steps =
                recipeDatabase.getRecipeSteps(recipeName);

        // This displays the recipe information
        txtRecipeName.setText(recipeName);
        txtIngredients.setText(ingredients);
        txtSteps.setText(steps);
    }
}