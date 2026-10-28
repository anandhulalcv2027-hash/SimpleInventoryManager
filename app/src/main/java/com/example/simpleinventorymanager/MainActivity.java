package com.example.simpleinventorymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etId, etName, etQuantity, etPrice;
    Button btnAdd, btnView, btnUpdate, btnDelete;
    TextView tvResult;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        tvResult = findViewById(R.id.tvResult);

        dbHelper = new DatabaseHelper(this);

        // ADD ITEM
        btnAdd.setOnClickListener(v -> {

            if (!validDetails()) return;

            String name = etName.getText().toString().trim();
            int quantity = Integer.parseInt(
                    etQuantity.getText().toString().trim());
            double price = Double.parseDouble(
                    etPrice.getText().toString().trim());

            boolean result = dbHelper.addItem(name, quantity, price);

            if (result) {
                showMessage("Item Added");
                clearFields();
                tvResult.setText(dbHelper.getAllItems());
            } else {
                showMessage("Failed to Add");
            }
        });

        // VIEW ITEMS
        btnView.setOnClickListener(v -> {
            tvResult.setText(dbHelper.getAllItems());
        });

        // UPDATE ITEM
        btnUpdate.setOnClickListener(v -> {

            if (!validId() || !validDetails()) return;

            int id = Integer.parseInt(
                    etId.getText().toString().trim());
            String name = etName.getText().toString().trim();
            int quantity = Integer.parseInt(
                    etQuantity.getText().toString().trim());
            double price = Double.parseDouble(
                    etPrice.getText().toString().trim());

            boolean result = dbHelper.updateItem(
                    id, name, quantity, price);

            if (result) {
                showMessage("Item Updated");
                clearFields();
                tvResult.setText(dbHelper.getAllItems());
            } else {
                showMessage("Item Not Found");
            }
        });

        // DELETE ITEM
        btnDelete.setOnClickListener(v -> {

            if (!validId()) return;

            int id = Integer.parseInt(
                    etId.getText().toString().trim());

            boolean result = dbHelper.deleteItem(id);

            if (result) {
                showMessage("Item Deleted");
                clearFields();
                tvResult.setText(dbHelper.getAllItems());
            } else {
                showMessage("Item Not Found");
            }
        });
    }

    // VALIDATE ID
    private boolean validId() {
        String id = etId.getText().toString().trim();

        if (id.isEmpty()) {
            etId.setError("Enter Item ID");
            return false;
        }

        try {
            if (Integer.parseInt(id) <= 0) {
                etId.setError("Enter a valid ID");
                return false;
            }
        } catch (NumberFormatException e) {
            etId.setError("Invalid ID");
            return false;
        }

        return true;
    }

    // VALIDATE ITEM DETAILS
    private boolean validDetails() {

        String name = etName.getText().toString().trim();
        String qty = etQuantity.getText().toString().trim();
        String price = etPrice.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter Item Name");
            return false;
        }

        if (qty.isEmpty()) {
            etQuantity.setError("Enter Quantity");
            return false;
        }

        if (price.isEmpty()) {
            etPrice.setError("Enter Price");
            return false;
        }

        try {
            if (Integer.parseInt(qty) < 0) {
                etQuantity.setError("Invalid Quantity");
                return false;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid Quantity");
            return false;
        }

        try {
            double p = Double.parseDouble(price);
            if (Double.isNaN(p) || Double.isInfinite(p) || p < 0) {
                etPrice.setError("Invalid Price");
                return false;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid Price");
            return false;
        }

        return true;
    }

    // SHOW TOAST MESSAGE
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {
        etId.setText("");
        etName.setText("");
        etQuantity.setText("");
        etPrice.setText("");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
