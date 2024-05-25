package com.example.tiendaonline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBconection extends SQLiteOpenHelper {

    public DBconection(Context context) {
        super(context, DBmanager.DB_NAME, null, DBmanager.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBmanager.TABLA_CLIENTE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DBmanager.TABLA_CLIENTE_DROP);
        onCreate(sqLiteDatabase);
    }
}
