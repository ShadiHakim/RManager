package com.example.rmanager.save;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "RECORDINGS";

    // Table columns
    public static final String _LOCATION = "_location";
    public static final String _CONTACTNAME = "_contactName";
    public static final String _DATE = "_date";
    public static final String _SAVED = "_saved";

    // Database Information
    static final String DB_NAME = "SAVED_RECORDINGS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _LOCATION + " TEXT PRIMARY KEY, " + _CONTACTNAME + " TEXT NOT NULL, " + _DATE + " TEXT NOT NULL, " + _SAVED + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}