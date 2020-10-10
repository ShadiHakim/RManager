package com.example.rmanager.save;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.rmanager.classes.Recording;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Recording recording) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._LOCATION, recording.get_location());
        contentValue.put(DatabaseHelper._CONTACTNAME, recording.get_contactName());
        contentValue.put(DatabaseHelper._DATE, recording.get_date());
        contentValue.put(DatabaseHelper._SAVED, recording.is_saved());
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._LOCATION, DatabaseHelper._CONTACTNAME, DatabaseHelper._DATE, DatabaseHelper._SAVED };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

//    public int update(String _location, String _contactName, String _date, Boolean _saved) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper._LOCATION, _location);
//        contentValues.put(DatabaseHelper._CONTACTNAME, _contactName);
//        contentValues.put(DatabaseHelper._DATE, _date);
//        contentValues.put(DatabaseHelper._SAVED, _saved);
//        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._LOCATION + " = " + _location, null);
//        return i;
//    }

    public void delete(String _location) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._LOCATION + " = \"" + _location + "\"", null);
    }

}