package com.example.mobileapp700;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RecipeMatcher {

    DatabaseHelper databaseHelper;

    public RecipeMatcher(Context context) {

        // This connects to the database
        databaseHelper = new DatabaseHelper(context);
    }

    // This gets recipes that can be made from the pantry
    public ArrayList<String> getMatchingRecipes() {

        ArrayList<String> matchingRecipes = new ArrayList<>();

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        // This gets all the recipes
        Cursor recipes = db.rawQuery(
                "SELECT id, name FROM recipes",
                null
        );

        while (recipes.moveToNext()) {

            int recipeId = recipes.getInt(0);
            String recipeName = recipes.getString(1);

            boolean canMakeRecipe = true;

            // This gets the ingredients needed for the recipe
            Cursor ingredients = db.rawQuery(
                    "SELECT ingredient, quantity, unit " +
                            "FROM recipe_ingredients WHERE recipe_id = ?",
                    new String[]{String.valueOf(recipeId)}
            );

            while (ingredients.moveToNext()) {

                String neededIngredient = ingredients.getString(0);
                double neededQuantity = ingredients.getDouble(1);
                String neededUnit = ingredients.getString(2);

                boolean ingredientFound = false;

                // This gets all the pantry items
                Cursor pantry = db.rawQuery(
                        "SELECT ingredient, quantity, unit FROM pantry",
                        null
                );

                while (pantry.moveToNext()) {

                    String pantryIngredient = pantry.getString(0);
                    double pantryQuantity = pantry.getDouble(1);
                    String pantryUnit = pantry.getString(2);

                    // This checks the ingredient name and unit
                    if (sameIngredient(
                            pantryIngredient,
                            neededIngredient) &&
                            sameUnitType(
                                    pantryUnit,
                                    neededUnit)) {

                        double pantryAmount =
                                convertQuantity(
                                        pantryQuantity,
                                        pantryUnit
                                );

                        double neededAmount =
                                convertQuantity(
                                        neededQuantity,
                                        neededUnit
                                );

                        // This checks if there is enough
                        if (pantryAmount >= neededAmount) {

                            ingredientFound = true;
                            break;
                        }
                    }
                }

                pantry.close();

                // The recipe cannot be made if an ingredient is missing
                if (!ingredientFound) {

                    canMakeRecipe = false;
                    break;
                }
            }

            ingredients.close();

            // This adds the recipe if all ingredients are available
            if (canMakeRecipe) {
                matchingRecipes.add(recipeName);
            }
        }

        recipes.close();

        return matchingRecipes;
    }

    // This checks ingredient names
    private boolean sameIngredient(
            String first,
            String second
    ) {

        first = first.toLowerCase().trim();
        second = second.toLowerCase().trim();

        if (first.equals(second)) {
            return true;
        }

        // This allows simple singular and plural names
        if (first.endsWith("s")) {
            first = first.substring(
                    0,
                    first.length() - 1
            );
        }

        if (second.endsWith("s")) {
            second = second.substring(
                    0,
                    second.length() - 1
            );
        }

        return first.equals(second);
    }

    // This converts quantities to common units
    private double convertQuantity(
            double quantity,
            String unit
    ) {

        unit = unit.toLowerCase().trim();

        // This converts kilograms to grams
        if (unit.equals("kg")) {
            return quantity * 1000;
        }

        // This converts litres to millilitres
        if (unit.equals("l") ||
                unit.equals("litre") ||
                unit.equals("litres")) {

            return quantity * 1000;
        }

        return quantity;
    }

    // This checks if the units can be compared
    private boolean sameUnitType(
            String first,
            String second
    ) {

        first = first.toLowerCase().trim();
        second = second.toLowerCase().trim();

        // This checks weight units
        if ((first.equals("g") || first.equals("kg")) &&
                (second.equals("g") || second.equals("kg"))) {

            return true;
        }

        // This checks liquid units
        if ((first.equals("ml") ||
                first.equals("l") ||
                first.equals("litre") ||
                first.equals("litres")) &&
                (second.equals("ml") ||
                        second.equals("l") ||
                        second.equals("litre") ||
                        second.equals("litres"))) {

            return true;
        }

        // Other units must be the same
        return first.equals(second);
    }
}