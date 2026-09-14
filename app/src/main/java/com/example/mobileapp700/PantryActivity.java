package com.example.mobileapp700;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    EditText txtIngredient;
    EditText txtQuantity;
    EditText txtUnit;
    EditText txtExpiryDate;

    Button btnAdd;

    ListView listPantry;

    ArrayList<String> pantryItems;
    ArrayAdapter<String> adapter;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the pantry screen
        setContentView(R.layout.activity_pantry);

        // This gets the input fields
        txtIngredient = findViewById(R.id.txtIngredient);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtUnit = findViewById(R.id.txtUnit);
        txtExpiryDate = findViewById(R.id.txtExpiryDate);

        // This gets the add button
        btnAdd = findViewById(R.id.btnAdd);

        // This gets the pantry list
        listPantry = findViewById(R.id.listPantry);

        // This creates the pantry list
        pantryItems = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pantryItems
        );

        listPantry.setAdapter(adapter);

        // This button adds or updates an item
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ingredient = txtIngredient.getText().toString();
                String quantity = txtQuantity.getText().toString();
                String unit = txtUnit.getText().toString();
                String expiryDate = txtExpiryDate.getText().toString();

                if (ingredient.isEmpty() || quantity.isEmpty() || unit.isEmpty()) {

                    Toast.makeText(
                            PantryActivity.this,
                            "Please enter ingredient, quantity and unit",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    String item = ingredient + " - " + quantity + " " + unit;

                    if (!expiryDate.isEmpty()) {
                        item = item + " - Expiry: " + expiryDate;
                    }

                    // This updates an existing item
                    if (selectedPosition != -1) {

                        pantryItems.set(selectedPosition, item);

                        selectedPosition = -1;
                        btnAdd.setText("Add Item");

                        Toast.makeText(
                                PantryActivity.this,
                                "Item updated",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        // This adds a new item
                        pantryItems.add(item);

                        Toast.makeText(
                                PantryActivity.this,
                                "Item added",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    adapter.notifyDataSetChanged();

                    // This clears the input fields
                    txtIngredient.setText("");
                    txtQuantity.setText("");
                    txtUnit.setText("");
                    txtExpiryDate.setText("");
                }
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

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(PantryActivity.this);

                builder.setTitle("Choose an option");

                builder.setItems(options, (dialog, which) -> {

                    // This edits the selected item
                    if (which == 0) {

                        String item = pantryItems.get(position);

                        String[] parts = item.split(" - ");

                        txtIngredient.setText(parts[0]);

                        String[] quantityAndUnit = parts[1].split(" ", 2);

                        txtQuantity.setText(quantityAndUnit[0]);

                        if (quantityAndUnit.length > 1) {
                            txtUnit.setText(quantityAndUnit[1]);
                        }

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

                    // This deletes the selected item
                    if (which == 1) {

                        AlertDialog.Builder deleteBuilder =
                                new AlertDialog.Builder(PantryActivity.this);

                        deleteBuilder.setTitle("Delete Item");

                        deleteBuilder.setMessage(
                                "Do you want to delete this item?"
                        );

                        deleteBuilder.setPositiveButton(
                                "Yes",
                                (deleteDialog, deleteWhich) -> {

                                    pantryItems.remove(position);
                                    adapter.notifyDataSetChanged();

                                    Toast.makeText(
                                            PantryActivity.this,
                                            "Item deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                        );

                        deleteBuilder.setNegativeButton("No", null);

                        deleteBuilder.show();
                    }
                });

                builder.show();
            }
        });
    }
}