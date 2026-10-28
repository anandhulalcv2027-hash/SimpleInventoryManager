package com.example.simpleinventorymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "InventoryDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity INTEGER, " +
                "price REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // No upgrade needed for this basic project.
    }

    // ADD ITEM
    public boolean addItem(String name, int quantity, double price) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("price", price);

        long result = db.insert("Inventory", null, values);

        return result != -1;
    }

    // VIEW ALL ITEMS
    public String getAllItems() {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Inventory", null);

        StringBuilder items = new StringBuilder();

        try {
            while (cursor.moveToNext()) {

                items.append("ID: ")
                        .append(cursor.getInt(0))
                        .append("\nName: ")
                        .append(cursor.getString(1))
                        .append("\nQuantity: ")
                        .append(cursor.getInt(2))
                        .append("\nPrice: ")
                        .append(cursor.getDouble(3))
                        .append("\n\n");
            }
        } finally {
            cursor.close();
        }

        if (items.length() == 0) {
            return "No items found";
        }

        return items.toString();
    }

    // UPDATE ITEM
    public boolean updateItem(int id, String name,
                              int quantity, double price) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("price", price);

        int result = db.update(
                "Inventory",
                values,
                "id=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }

    // DELETE ITEM
    public boolean deleteItem(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                "Inventory",
                "id=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}