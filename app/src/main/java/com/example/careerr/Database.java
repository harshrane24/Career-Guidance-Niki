package com.example.careerr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProFolio.db";
    private static final String TABLE_NAME = "personal_details";
    private static final int DATABASE_VERSION = 1;

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_EDUCATION = "education";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_DOB = "dob";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_EDUCATION + " TEXT, " +
                COLUMN_CONTACT + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_DOB + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String fullName, String address, String education, String contact, String email, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_NAME, fullName);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_EDUCATION, education);
        contentValues.put(COLUMN_CONTACT, contact);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_DOB, dob);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // returns true if data is inserted, false otherwise
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean updateData(String id, String fullName, String address, String education, String contact, String email, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_NAME, fullName);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_EDUCATION, education);
        contentValues.put(COLUMN_CONTACT, contact);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_DOB, dob);

        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[]{id});
    }
}




