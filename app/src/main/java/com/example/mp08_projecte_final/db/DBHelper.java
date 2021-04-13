package com.example.mp08_projecte_final.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {
    // database version
    private static final int database_VERSION = 6;

    // database name
    private static final String database_NAME = "buidem";

    private String[] tables = new String[]{
            "CREATE TABLE machines ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT UNIQUE NOT NULL," +
                    "client_name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "zip_code TEXT NOT NULL," +
                    "city TEXT NOT NULL," +
                    "telf TEXT," +
                    "email TEXT," +
                    "serial_number TEXT NOT NULL," +
                    "last_check DATE," +
                    "id_zone INT NOT NULL," +
                    "id_type INT NOT NULL)",

            "CREATE TABLE zones ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "lat DECIMAL(9,6)," +
                    "lon Decimal(8,6)," +
                    "description DATE NOT NULL)",

            "CREATE TABLE types ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL)"
    };

    public DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTables(db);
    }

    public void createTables(SQLiteDatabase db) {
        for(String table : this.tables) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this.createTables(db);
        //Toast.makeText(MainActivity.class, "Database updated v" + oldVersion + " > " + newVersion, Toast.LENGTH_SHORT).show();
    }
}
