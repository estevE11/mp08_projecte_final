package com.example.mp08_projecte_final.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {
    // database version
    private static final int database_VERSION = 1;

    // database name
    private static final String database_NAME = "store";

    public DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] tables = new String[]{
                "CREATE TABLE machines ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE NOT NULL," +
                        "client_name TEXT NOT NULL," +
                        "address TEXT NOT NULL," +
                        "zip_code TEXT NOT NULL," +
                        "city TEXT NOT NULL," +
                        "description TEXT NOT NULL," +
                        "telf TEXT NOT NULL," +
                        "email TEXT," +
                        "serial_number TEXT NOT NULL," +
                        "last_check DATE," +
                        "id_zone INT NOT NULL," +
                        "id_type INT NOT NULL)",

                "CREATE TABLE zones ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "longitude DECIMAL(9,6) NOT NULL," +
                        "latitude Decimal(8,6) NOT NULL," +
                        "description DATE NOT NULL)",

                "CREATE TABLE types ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL)"
        };

        for(String table : tables) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Toast.makeText(MainActivity.class, "Database updated v" + oldVersion + " > " + newVersion, Toast.LENGTH_SHORT).show();
    }
}
