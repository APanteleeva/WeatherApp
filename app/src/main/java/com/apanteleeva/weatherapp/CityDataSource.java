package com.apanteleeva.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CityDataSource {
    private WeatherAppDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private String[] citiesAllColumn = {
            WeatherAppDatabaseHelper.COL_ID,
            WeatherAppDatabaseHelper.COL_NAME
    };

    public CityDataSource (Context context ){
        dbHelper = new WeatherAppDatabaseHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public City addCity(String note) {
        ContentValues values = new ContentValues();
        values.put(WeatherAppDatabaseHelper.COL_NAME, note);
        long insertId = database.insert(WeatherAppDatabaseHelper.TABLE_CITY, null,
                values);
        City newCity = new City();
        newCity.setName(note);
        newCity.setId(insertId);
        return newCity;
    }

    public void editCity ( long id , String note ) {
        ContentValues editedNote = new ContentValues ();
        editedNote.put(dbHelper.COL_ID , id );
        editedNote.put(dbHelper.COL_NAME , note );
        database.update(dbHelper.TABLE_CITY,
                editedNote,
                dbHelper.COL_ID + "=" + id,
                null );
    }

    public boolean deleteCity(long id) {
        //long id = city.getId ();
        int result=database.delete(WeatherAppDatabaseHelper.TABLE_CITY, WeatherAppDatabaseHelper.COL_ID + " = " + id, null);
        if(result>0){
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAll () {
        int result =database.delete(WeatherAppDatabaseHelper.TABLE_CITY, null, null);
        if (result>0){
            return true;
        }else{
            return false;
        }
    }
    public List<City> getAllRecords(){
        List<City> cityList = new ArrayList<>();
        Cursor cursor = database.query(WeatherAppDatabaseHelper.TABLE_CITY,citiesAllColumn,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            City city = cursorToRow(cursor);
            cityList.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return cityList;
    }
    private City cursorToRow ( Cursor cursor ) {
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setName(cursor.getString(1));
        return city;
    }
}
