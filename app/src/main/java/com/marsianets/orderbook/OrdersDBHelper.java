package com.marsianets.orderbook;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersDBHelper extends SQLiteOpenHelper implements BaseColumns {

    private SQLiteDatabase db;
    private static String DB_ORDERS = "orders.db";
    public static final String TABLE_NAME = "orders";
    private ArrayList<String> columns = Order.getColumns();
    private HashMap<String, String> columnTypes = Order.getColumnTypes();

    public OrdersDBHelper(Context context) {
        super(context, DB_ORDERS, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder("CREATE TABLE " + TABLE_NAME);
        query.append(" (_id INTEGER PRIMARY KEY NOT NULL");
        for (String column : columns) {
            query.append(", ");
            query.append(column);
            query.append(" ");
            query.append(columnTypes.get(column));
        }
        query.append("); ");
        db.execSQL(query.toString());
        //db.execSQL("CREATE  TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        //+ NAME + " TEXT, "+ CAR_MAKER + " TEXT, " + COLOR_CODE + " TEXT); ");

    }

    public static SQLiteDatabase openDB(int flag) {
        OrdersDBHelper dbHelper = new OrdersDBHelper(MainActivity.appContext);

        if (flag == SQLiteDatabase.OPEN_READWRITE) {
            try {
                dbHelper.db = dbHelper.getWritableDatabase();
            } catch (SQLiteException ex) {
                Log.d("OrdersDBHelper", "Can't open database in write mode");
            }
        } else if (flag == SQLiteDatabase.OPEN_READONLY) {
            dbHelper.db = dbHelper.getReadableDatabase();
        } else throw new IllegalArgumentException("openDB(int flag): flag may only be " +
                "SQLiteDatabase.OPEN_READONLY or SQLiteDatabase.OPEN_READWRITE");


        return dbHelper.db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

}
