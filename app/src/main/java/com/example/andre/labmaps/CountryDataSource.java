package com.example.andre.labmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiag_000 on 03/04/2017.
 */

public class CountryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.COLUMN_NAME,
                                    MySQLiteHelper.COLUMN_LATITUDE,
                                    MySQLiteHelper.COLUMN_LONGITUDE};

    public CountryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void createCountry(String name, double latitude, double longitude) {
        if(getCountry(name) == null) {

            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_NAME, name);
            values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
            values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
            long insertId = database.insert(MySQLiteHelper.TABLE_COUNTRIES, null,
                    values);

        }
    }

    public Country getCountry(String name) {
        try {
            Cursor cursor = database.query(MySQLiteHelper.TABLE_COUNTRIES,
                    allColumns, MySQLiteHelper.COLUMN_NAME + " = " + "'"+name+"'", null,
                    null, null, null);
            if (cursor.moveToFirst()) {
                Country newCountry = cursorToCountry(cursor);
                cursor.close();
                return newCountry;
            }
        }catch(Exception e){
            System.out.println("naoachou");
            return(null);
        }
        return(null);
    }

    public void clearDatabase() {

        database.delete(MySQLiteHelper.TABLE_COUNTRIES, null, null);
    }


    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<Country>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COUNTRIES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Country country = cursorToCountry(cursor);
            countries.add(country);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return countries;
    }

    private Country cursorToCountry(Cursor cursor) {
        Country country = new Country();
        country.setName(cursor.getString(1));
        country.setLatitude(cursor.getDouble(2));
        country.setLongitude(cursor.getDouble(3));
        return country;
    }
}

