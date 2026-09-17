package com.example.mobileapp700;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    EditText txtIngredient, txtQuantity, txtUnit, txtExpiryDate;
    Button btnAdd;
    ListView listPantry;

    ArrayList<String> pantryItems;
    ArrayList<Integer> pantryIds;
    ArrayAdapter<String> adapter;

    DatabaseHelper databaseHelper;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the pantry screen
        setContentView(R.layout.activity_pantry);

        // This gets the fields and buttons
        txtIngredient = findViewById(R.id.txtIngredient);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtUnit = findViewById(R.id.txtUnit);
        txtExpiryDate = findViewById(R.id.txtExpiryDate);
        btnAdd = findViewById(R.id.btnAdd);
        listPantry = findViewById(R.id.listPantry);

        // This connects to the database
        databaseHelper = new DatabaseHelper(this);

        // This loads the pantry items
        pantryItems = databaseHelper.getPantryItems();
        pantryIds = databaseHelper.getPantryIds();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pantryItems
        );

        listPantry.setAdapter(adapter);

        // This adds or updates an item
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ingredient = txtIngredient.getText().toString();
                String quantity = txtQuantity.getText().toString();
                String unit = txtUnit.getText().toString();
                String expiryDate = txtExpiryDate.getText().toString();

                // This checks the required fields
                if (ingredient.isEmpty() || quantity.isEmpty() || unit.isEmpty()) {
                    Toast.makeText(
                            PantryActivity.this,
                            "Please enter ingredient, quantity and unit",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                double quantityNumber = Double.parseDouble(quantity);

                // This checks the quantity
                if (quantityNumber <= 0) {
                    Toast.makeText(
                            PantryActivity.this,
                            "Quantity must be greater than 0",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // This checks the expiry date
                if (!expiryDate.isEmpty()) {

                    try {
                        SimpleDateFormat dateFormat =
                                new SimpleDateFormat("yyyy-MM-dd");

                        dateFormat.setLenient(false);
                        dateFormat.parse(expiryDate);

                    } catch (Exception e) {

                        Toast.makeText(
                                PantryActivity.this,
                                "Please enter the expiry date as yyyy-MM-dd",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }
                }

                String item = ingredient + " - " + quantity + " " + unit;

                if (!expiryDate.isEmpty()) {
                    item += " - Expiry: " + expiryDate;
                }

                // This updates an item
                if (selectedPosition != -1) {

                    int itemId = pantryIds.get(selectedPosition);

                    boolean updated = databaseHelper.updatePantryItem(
                            itemId,
                            ingredient,
                            quantityNumber,
                            unit,
                            expiryDate
                    );

                    if (updated) {
                        pantryItems.set(selectedPosition, item);
                        selectedPosition = -1;
                        btnAdd.setText("Add Item");

                        Toast.makeText(
                                PantryActivity.this,
                                "Item updated",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    // This adds a new item
                    boolean saved = databaseHelper.addPantryItem(
                            ingredient,
                            quantityNumber,
                            unit,
                            expiryDate
                    );

                    if (saved) {
                        pantryItems = databaseHelper.getPantryItems();
                        pantryIds = databaseHelper.getPantryIds();

                        adapter.clear();
                        adapter.addAll(pantryItems);

                        Toast.makeText(
                                PantryActivity.this,
                                "Item added",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                adapter.notifyDataSetChanged();

                // This clears the fields
                txtIngredient.setText("");
                txtQuantity.setText("");
                txtUnit.setText("");
                txtExpiryDate.setText("");
            }
        });

        // This lets the user edit or delete an item
        listPantry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {

                String[] options = {"Edit", "Delete"};

                new AlertDialog.Builder(PantryActivity.this)
                        .setTitle("Choose an option")
                        .setItems(options, (dialog, which) -> {

                            // This edits the item
                            if (which == 0) {

                                String[] parts =
                                        pantryItems.get(position).split(" - ");

                                txtIngredient.setText(parts[0]);

                                String[] quantityAndUnit =
                                        parts[1].split(" ", 2);

                                txtQuantity.setText(quantityAndUnit[0]);
                                txtUnit.setText(quantityAndUnit[1]);

                                if (parts.length > 2) {
                                    txtExpiryDate.setText(
                                            parts[2].replace("Expiry: ", "")
                                    );
                                } else {
                                    txtExpiryDate.setText("");
                                }

                                selectedPosition = position;
                                btnAdd.setText("Update Item");
                            }

                            // This deletes the item
                            if (which == 1) {

                                new AlertDialog.Builder(PantryActivity.this)
                                        .setTitle("Delete Item")
                                        .setMessage("Do you want to delete this item?")
                                        .setPositiveButton("Yes", (d, w) -> {

                                            int itemId =
                                                    pantryIds.get(position);

                                            boolean deleted =
                                                    databaseHelper.deletePantryItem(itemId);

                                            if (deleted) {
                                                pantryItems.remove(position);
                                                pantryIds.remove(position);
                                                adapter.notifyDataSetChanged();

                                                Toast.makeText(
                                                        PantryActivity.this,
                                                        "Item deleted",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        })
                        .show();
            }
        });
    }
}