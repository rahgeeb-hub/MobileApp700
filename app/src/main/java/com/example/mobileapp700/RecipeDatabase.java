package com.example.mobileapp700;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecipeDatabase {

    DatabaseHelper databaseHelper;

    public RecipeDatabase(Context context) {

        // This connects to the database
        databaseHelper = new DatabaseHelper(context);
    }

    // This adds the starting recipes
    public void seedRecipes() {

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();

        // Recipes are only added when the table is empty
        if (count == 0) {

            addRecipe(
                    db,
                    "Scrambled Eggs",
                    "1. Beat the eggs and milk.\n" +
                            "2. Melt the butter in a pan.\n" +
                            "3. Add the egg mixture.\n" +
                            "4. Cook until done.",
                    new String[]{"Egg", "Milk", "Butter"},
                    new double[]{2, 50, 10},
                    new String[]{"each", "ml", "g"}
            );

            addRecipe(
                    db,
                    "Boiled Eggs",
                    "1. Place eggs in water.\n" +
                            "2. Boil for about 8 minutes.\n" +
                            "3. Cool and peel.",
                    new String[]{"Egg"},
                    new double[]{2},
                    new String[]{"each"}
            );

            addRecipe(
                    db,
                    "Butter Toast",
                    "1. Toast the bread.\n" +
                            "2. Spread butter over the toast.",
                    new String[]{"Bread", "Butter"},
                    new double[]{2, 10},
                    new String[]{"slice", "g"}
            );

            addRecipe(
                    db,
                    "Cheese Sandwich",
                    "1. Place cheese between two slices of bread.\n" +
                            "2. Serve the sandwich.",
                    new String[]{"Bread", "Cheese"},
                    new double[]{2, 2},
                    new String[]{"slice", "slice"}
            );

            addRecipe(
                    db,
                    "Egg Sandwich",
                    "1. Cook the egg.\n" +
                            "2. Place the egg between the bread slices.",
                    new String[]{"Bread", "Egg"},
                    new double[]{2, 1},
                    new String[]{"slice", "each"}
            );

            addRecipe(
                    db,
                    "Grilled Cheese",
                    "1. Place cheese between bread slices.\n" +
                            "2. Spread butter outside.\n" +
                            "3. Grill until golden.",
                    new String[]{"Bread", "Cheese", "Butter"},
                    new double[]{2, 2, 10},
                    new String[]{"slice", "slice", "g"}
            );

            addRecipe(
                    db,
                    "Porridge",
                    "1. Add oats and milk to a pot.\n" +
                            "2. Cook while stirring.\n" +
                            "3. Serve when thick.",
                    new String[]{"Oats", "Milk"},
                    new double[]{50, 250},
                    new String[]{"g", "ml"}
            );

            addRecipe(
                    db,
                    "Pancakes",
                    "1. Mix flour, milk and egg.\n" +
                            "2. Pour batter into a hot pan.\n" +
                            "3. Cook both sides.",
                    new String[]{"Flour", "Milk", "Egg"},
                    new double[]{100, 150, 1},
                    new String[]{"g", "ml", "each"}
            );

            addRecipe(
                    db,
                    "Tuna Sandwich",
                    "1. Place tuna on the bread.\n" +
                            "2. Close the sandwich and serve.",
                    new String[]{"Bread", "Tuna"},
                    new double[]{2, 100},
                    new String[]{"slice", "g"}
            );

            addRecipe(
                    db,
                    "Tomato Pasta",
                    "1. Boil the pasta.\n" +
                            "2. Heat the tomato sauce.\n" +
                            "3. Mix together and serve.",
                    new String[]{"Pasta", "Tomato Sauce"},
                    new double[]{100, 150},
                    new String[]{"g", "ml"}
            );

            addRecipe(
                    db,
                    "Butter Pasta",
                    "1. Boil the pasta.\n" +
                            "2. Drain the pasta.\n" +
                            "3. Mix in the butter.",
                    new String[]{"Pasta", "Butter"},
                    new double[]{100, 15},
                    new String[]{"g", "g"}
            );

            addRecipe(
                    db,
                    "Rice and Vegetables",
                    "1. Cook the rice.\n" +
                            "2. Cook the vegetables.\n" +
                            "3. Mix together and serve.",
                    new String[]{"Rice", "Vegetables"},
                    new double[]{100, 100},
                    new String[]{"g", "g"}
            );

            addRecipe(
                    db,
                    "Fried Rice",
                    "1. Cook the rice.\n" +
                            "2. Fry the egg.\n" +
                            "3. Add rice and vegetables.\n" +
                            "4. Mix and cook.",
                    new String[]{"Rice", "Egg", "Vegetables"},
                    new double[]{100, 1, 100},
                    new String[]{"g", "each", "g"}
            );

            addRecipe(
                    db,
                    "Mashed Potatoes",
                    "1. Boil the potatoes until soft.\n" +
                            "2. Add milk and butter.\n" +
                            "3. Mash until smooth.",
                    new String[]{"Potato", "Milk", "Butter"},
                    new double[]{300, 50, 10},
                    new String[]{"g", "ml", "g"}
            );

            addRecipe(
                    db,
                    "Baked Potato",
                    "1. Wash the potato.\n" +
                            "2. Bake until soft.\n" +
                            "3. Add butter before serving.",
                    new String[]{"Potato", "Butter"},
                    new double[]{250, 10},
                    new String[]{"g", "g"}
            );
        }
    }

    // This adds one recipe and its ingredients
    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String steps,
            String[] ingredients,
            double[] quantities,
            String[] units
    ) {

        ContentValues recipe =
                new ContentValues();

        recipe.put("name", name);
        recipe.put("steps", steps);

        long recipeId = db.insert(
                "recipes",
                null,
                recipe
        );

        for (int i = 0; i < ingredients.length; i++) {

            addRecipeIngredient(
                    db,
                    recipeId,
                    ingredients[i],
                    quantities[i],
                    units[i]
            );
        }
    }

    // This adds an ingredient needed for a recipe
    private void addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredient,
            double quantity,
            String unit
    ) {

        ContentValues values =
                new ContentValues();

        values.put("recipe_id", recipeId);
        values.put("ingredient", ingredient);
        values.put("quantity", quantity);
        values.put("unit", unit);

        db.insert(
                "recipe_ingredients",
                null,
                values
        );
    }
}