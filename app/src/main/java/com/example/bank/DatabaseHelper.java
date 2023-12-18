package com.example.bank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Bank.db";
    public static final String TABLE_NAME = "transactions_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TYPE";
    public static final String COL_3 = "AMOUNT";
    public static final String COL_4 = "ACCOUNT";
    public static final String COL_5 = "DATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TYPE TEXT, AMOUNT INTEGER, ACCOUNT TEXT, DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public boolean insertData(String type, String amount, String account, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, type);
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, account);
        contentValues.put(COL_5, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public int getAccountBalance(String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        int balance = 0;
        Cursor cursor = db.rawQuery("SELECT TYPE, AMOUNT FROM " + TABLE_NAME + " WHERE ACCOUNT = ?", new String[]{account});
        while(cursor.moveToNext()) {
            String type = cursor.getString(0);
            int amount = cursor.getInt(1);
            if(type.equals("Debit")) {
                balance -= amount;
            } else {
                balance += amount;
            }
        }
        cursor.close();
        return balance;
    }

    public boolean transferFunds(String sourceAccount, String targetAccount, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int sourceBalance = getAccountBalance(sourceAccount);
            if (sourceBalance < amount) {
                return false;
            }
            insertData("Debit", String.valueOf(amount), sourceAccount, getCurrentDate());

            insertData("Credit", String.valueOf(amount), targetAccount, getCurrentDate());

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    private String getCurrentDate() {
        // Implement a method to get the current date in required format
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }



}
