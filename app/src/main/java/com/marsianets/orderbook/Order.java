package com.marsianets.orderbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;


public class Order {

    private int orderId;
    private Client client;
    private TopCoat topCoat;
    private String orderType;
    private Float volumeBase;
    private Float volumeAddition;
    private String execDate;
    private String orderDate;
    private boolean execState;
    private HashMap<String, Object> orderValues;
    private boolean isSaved;

    Order() {
        orderId = Order.getOrdersCount() + 1;
        client = new Client();
        topCoat = new TopCoat();
        this.setOrderType("Test");
        this.setVolumeBase(0.0f);
        this.setVolumeAddition(0.0f);
        Date now = new Date();
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        this.setExecDate(dateFormatter.format(now));
        this.setOrderDate(dateFormatter.format(now));
        execState = false;
        isSaved = false;


    }

    Order(HashMap<String, Object> orderValues) {
        client = new Client();
        try {
            this.setOrderId(Integer.parseInt(orderValues.get("_id").toString()));
            client.setName(orderValues.get("clientName").toString());
            client.setPhone(orderValues.get("clientPhone").toString());
            try {
                this.setExecDate(orderValues.get("execDate").toString());
            } catch (NullPointerException ex) {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                this.setExecDate(dateFormat.format(new Date()));
            }

            try {
                this.setOrderDate(orderValues.get("orderDate").toString());
            } catch (NullPointerException ex) {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                this.setOrderDate(dateFormat.format(new Date()));
            }

            this.setOrderType(orderValues.get("orderType").toString());
            this.setVolumeBase(Float.valueOf(orderValues.get("volumeBase").toString()));
            this.setVolumeAddition(Float.valueOf(orderValues.get("volumeAddition").toString()));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        topCoat = new TopCoat();
        try {
            topCoat.setCarMaker(orderValues.get("carMaker").toString());
            topCoat.setColorCode(orderValues.get("colorCode").toString());
        } catch (CursorIndexOutOfBoundsException ex) {
            ex.printStackTrace(System.err);
        }
        this.setExecState(Boolean.parseBoolean(String.valueOf(orderValues.get("execState"))));
        isSaved = true;

    }

    public static void deleteOrderByID(int deletingOrderId) {
        SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READWRITE);
        db.delete(OrdersDBHelper.TABLE_NAME, "_id=" + deletingOrderId, null);

        db.close();
    }

    public static ArrayList<Order> getAllOrdersFromBase() {
        ArrayList<Order> orderArrayList = new ArrayList<Order>();
        HashMap<String, Object> orderValues;
        SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM " + OrdersDBHelper.TABLE_NAME + ";";
        Cursor selectedOrders = db.rawQuery(query, null);
        try {
            for (selectedOrders.moveToFirst(); !selectedOrders.isAfterLast(); selectedOrders.moveToNext()) {
                orderValues = new HashMap<String, Object>();
                for (int columnIndex = 0; columnIndex < selectedOrders.getColumnCount(); columnIndex++) {
                    orderValues.put(selectedOrders.getColumnName(columnIndex), selectedOrders.getString(columnIndex));
                }
                Order mOrder = new Order(orderValues);
                orderArrayList.add(mOrder);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
        }

        selectedOrders.close();
        db.close();
        return orderArrayList;
    }

    public String getCarMaker() {
        return this.topCoat.getCarMaker();
    }

    public String getClientPhone() {
        return this.client.getPhone();
    }

    public String getClientName() {
        return this.client.getName();
    }

    public String getColorCode() {
        return this.topCoat.getColorCode();
    }


    public static ArrayList<Order> getOrdersFromBase(String date) {
        ArrayList<Order> orderArrayList = new ArrayList<Order>();
        HashMap<String, Object> orderValues;
        SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM " + OrdersDBHelper.TABLE_NAME + " WHERE execDate = '" + date + "';";
        Cursor selectedOrders = db.rawQuery(query, null);
        try {
            for (selectedOrders.moveToFirst(); !selectedOrders.isAfterLast(); selectedOrders.moveToNext()) {
                orderValues = new HashMap<String, Object>();
                for (int columnIndex = 0; columnIndex < selectedOrders.getColumnCount(); columnIndex++) {
                    orderValues.put(selectedOrders.getColumnName(columnIndex), selectedOrders.getString(columnIndex));
                }
                Order mOrder = new Order(orderValues);
                orderArrayList.add(mOrder);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
        }

        selectedOrders.close();
        db.close();
        return orderArrayList;
    }

    public static Order getOrderFromBase(int id) {
        HashMap<String, Object> orderValues;
        Order mOrder = new Order();
        SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM " + OrdersDBHelper.TABLE_NAME + " WHERE _id=" + id + ";";
        Cursor selectedOrders = db.rawQuery(query, null);
        try {
            if (selectedOrders.moveToFirst()) {
                orderValues = new HashMap<String, Object>();
                for (int columnIndex = 0; columnIndex < selectedOrders.getColumnCount(); columnIndex++) {
                    orderValues.put(selectedOrders.getColumnName(columnIndex), selectedOrders.getString(columnIndex));
                }
                mOrder = new Order(orderValues);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
        } catch (CursorIndexOutOfBoundsException ex) {
            ex.printStackTrace(System.err);
        }
        selectedOrders.close();
        db.close();

        return mOrder;
    }

    public static Order getByName(String name) {
        return new Order();
    }

    public static ArrayList<String> getColumns() {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("clientName");
        columns.add("clientPhone");
        columns.add("carMaker");
        columns.add("colorCode");
        columns.add("orderType");
        columns.add("volumeBase");
        columns.add("volumeAddition");
        columns.add("execDate");
        columns.add("orderDate");
        columns.add("execState");

        return columns;
    }

    public static HashMap<String, String> getColumnTypes() {
        HashMap<String, String> columnTypes = new HashMap<String, String>();
        columnTypes.put("clientName", "TEXT");
        columnTypes.put("carMaker", "TEXT");
        columnTypes.put("colorCode", "TEXT");
        columnTypes.put("execDate", "TEXT");
        columnTypes.put("orderDate", "TEXT");
        columnTypes.put("orderType", "TEXT");
        columnTypes.put("volumeBase", "REAL");
        columnTypes.put("volumeAddition", "REAL");
        columnTypes.put("clientPhone", "TEXT");
        columnTypes.put("execState", "TEXT");
        return columnTypes;
    }

    public int getOrderId() {
        return orderId;
    }

    public static int getOrdersCount() {
        SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT _id from " + OrdersDBHelper.TABLE_NAME;
        Cursor selectedID = db.rawQuery(query, null);
        int ordersCount = selectedID.getCount();

        db.close();

        return ordersCount;
    }

    public String getExecDate() {
        return execDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public boolean getExecState() {
        return execState;
    }

    public String getOrderType() {
        return orderType;
    }

    public HashMap<String, Object> getOrderValues() {
        orderValues = new HashMap<String, Object>();
        orderValues.put("_id", this.orderId);
        orderValues.put("clientName", this.client.getName());
        orderValues.put("clientPhone", this.client.getPhone());
        orderValues.put("carMaker", this.topCoat.getCarMaker());
        orderValues.put("colorCode", this.topCoat.getColorCode());
        orderValues.put("orderType", this.getOrderType());
        orderValues.put("volumeBase", this.getVolumeBase());
        orderValues.put("volumeAddition", this.getVolumeAddition());
        orderValues.put("execDate", this.getExecDate());
        orderValues.put("orderDate", this.getOrderDate());
        orderValues.put("execState", String.valueOf(this.getExecState()));
        return orderValues;
    }

    public Float getVolumeBase() {
        return volumeBase;
    }

    public Float getVolumeAddition() {
        return volumeAddition;
    }

    public boolean isEmpty() {
        return this.client.getName().equals("") && this.orderType.equals("");

    }

    public boolean save() {
        if (true) {
            Parcel parcel = Parcel.obtain();
            parcel.writeMap(this.getOrderValues());
            parcel.setDataPosition(0);
            ContentValues contentValues = ContentValues.CREATOR.createFromParcel(parcel);

            SQLiteDatabase db = OrdersDBHelper.openDB(SQLiteDatabase.OPEN_READWRITE);
            if (!isSaved) {
                db.insert(OrdersDBHelper.TABLE_NAME, null, contentValues);
                isSaved = true;
            } else {
                db.update(OrdersDBHelper.TABLE_NAME, contentValues, "_id=" + orderId, null);
            }


            db.close();
            return true;
        } else {
            return false;
        }
    }

    public void setExecDate(String date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        if (date == null || date.equals("")) {
            Date now = new Date();
            execDate = dateFormat.format(now);
        } else {
            try {
                this.execDate = dateFormat.format(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setExecDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        this.execDate = dateFormat.format(date);
    }

    public void setOrderDate(String date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        if (date == null || date.equals("")) {
            Date now = new Date();
            orderDate = dateFormat.format(now);
        } else {
            try {
                this.orderDate = dateFormat.format(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOrderDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        this.orderDate = dateFormat.format(date);
    }


    public void setExecState(int state) {
        if (state == 1) {
            this.execState = true;
        }
        this.execState = false;
    }

    public void setExecState(boolean state) {
        this.execState = state;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderValues(HashMap<String, Object> orderValues) {
        this.orderValues = orderValues;
        client.setName(this.orderValues.get("clientName").toString());
        client.setPhone(this.orderValues.get("clientPhone").toString());
        this.setExecDate(this.orderValues.get("execDate").toString());
        this.setOrderDate(this.orderValues.get("orderDate").toString());
        this.setOrderType(this.orderValues.get("orderType").toString());
        this.setVolumeBase(Float.valueOf(this.orderValues.get("volumeBase").toString()));
        this.setVolumeAddition(Float.valueOf(this.orderValues.get("volumeAddition").toString()));
        topCoat.setCarMaker(this.orderValues.get("carMaker").toString());
        topCoat.setColorCode(this.orderValues.get("colorCode").toString());
        this.setExecState(Boolean.parseBoolean(String.valueOf(this.orderValues.get("execState"))));
    }

    public void setVolumeBase(Float volumeBase) {
        this.volumeBase = volumeBase;
    }

    public void setVolumeAddition(Float volumeBase) {
        this.volumeAddition = volumeBase;
    }

    public static TreeMap<Date, ArrayList<Order>> sortByDate(ArrayList<Order> ordersToSort) {
        TreeMap<Date, ArrayList<Order>> sortedOrders = new TreeMap<Date, ArrayList<Order>>();
        HashSet<Date> unsortedDates = new HashSet<Date>();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        for (Order order : ordersToSort) {
            try {
                Date date = dateFormat.parse(order.getExecDate());
                unsortedDates.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (Date date : unsortedDates) {
            ArrayList<Order> ordersByDate = new ArrayList<Order>();
            for (Order order : ordersToSort) {
                if (order.getExecDate().equals(dateFormat.format(date))) {
                    ordersByDate.add(order);
                }
            }
            sortedOrders.put(date, ordersByDate);
        }

        return sortedOrders;
    }

}
