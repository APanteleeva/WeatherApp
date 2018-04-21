package com.apanteleeva.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherAppDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION=1;
    static final String TABLE_CITY = "cities";
    //название столбцов
    public static final String COL_ID = "city_id";
    public static final String COL_NAME = "name";

    public WeatherAppDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_CITY+" (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                 + COL_NAME + " TEXT );" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CITY);
        onCreate(db);
    }
}
