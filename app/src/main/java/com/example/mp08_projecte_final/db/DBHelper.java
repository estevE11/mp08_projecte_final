package com.example.mp08_projecte_final.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DBHelper extends SQLiteOpenHelper {
    // database version
    private static final int database_VERSION = 14;

    // database name
    private static final String database_NAME = "buidem";

    private String[] table_machines = new String[]{
            "name TEXT UNIQUE NOT NULL",
            "client_name TEXT NOT NULL",
            "address TEXT NOT NULL",
            "zip_code TEXT NOT NULL",
            "city TEXT NOT NULL",
            "telf TEXT",
            "email TEXT",
            "serial_number TEXT NOT NULL",
            "last_check DATE",
            "id_zone INT NOT NULL",
            "id_type INT NOT NULL",
    };

    private String[] table_zones = new String[]{
            "name TEXT NOT NULL",
            "lat DECIMAL(9,6)",
            "lon Decimal(8,6)",
            "description DATE NOT NULL"
    };

    private String[] table_types = new String[]{
            "name TEXT NOT NULL",
            "color TEXT"
    };

    public DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTable(db, "machines", this.table_machines);
        this.createTable(db, "zones", this.table_zones);
        this.createTable(db, "types", this.table_types);
    }

    public void createTable(SQLiteDatabase db, String table_name, String[] table_fields) {
        String query = "CREATE TABLE " + table_name + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT,";

        for(int i = 0; i < table_fields.length-1; i++) {
            query += table_fields[i] + ",";
        }
        query += table_fields[table_fields.length-1] + ")";

        db.execSQL(query);
    }

    public void upgradeTable(SQLiteDatabase db, String table_name, String[] table_fields) {
        for(int i = 0; i < table_fields.length; i++) {
            try {
                db.execSQL("ALTER TABLE " + table_name  + " ADD COLUMN " + table_fields[i]);
                Log.d("asdf", "Worked for " + table_fields[i] + " in " + table_name);
            } catch(Exception e) {
                Log.d("asdf", "Failed for " + table_fields[i] + " in " + table_name + " (" + e.getMessage() + ")");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("asdf", "Migrating from v" + oldVersion + " to v" + newVersion);
        this.upgradeTable(db, "machines", this.table_machines);
        this.upgradeTable(db, "zones", this.table_zones);
        this.upgradeTable(db, "types", this.table_types);
        //Toast.makeText(MainActivity.class, "Database updated v" + oldVersion + " > " + newVersion, Toast.LENGTH_SHORT).show();
    }
}
