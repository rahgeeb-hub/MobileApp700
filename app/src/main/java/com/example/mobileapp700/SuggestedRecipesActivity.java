package com.example.mobileapp700;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    ListView listRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the suggested recipes screen
        setContentView(R.layout.activity_suggested_recipes);

        // This gets the recipe list
        listRecipes = findViewById(R.id.listRecipes);

        // This finds recipes that match the pantry
        RecipeMatcher recipeMatcher =
                new RecipeMatcher(this);

        ArrayList<String> matchingRecipes =
                recipeMatcher.getMatchingRecipes();

        // This displays the matching recipes
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        matchingRecipes
                );

        listRecipes.setAdapter(adapter);

        // This displays a message when no recipes match
        if (matchingRecipes.isEmpty()) {

            Toast.makeText(
                    this,
                    "No recipes can be made with your current pantry",
                    Toast.LENGTH_LONG
            ).show();
        }

        // This opens the selected recipe
        listRecipes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        String recipeName =
                                matchingRecipes.get(position);

                        Intent intent = new Intent(
                                SuggestedRecipesActivity.this,
                                RecipeDetailActivity.class
                        );

                        intent.putExtra(
                                "recipeName",
                                recipeName
                        );

                        startActivity(intent);
                    }
                });
    }
}