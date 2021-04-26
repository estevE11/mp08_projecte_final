package com.example.mp08_projecte_final.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBDatasource {
    private DBHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    // CONSTRUCTOR
    public DBDatasource(Context ctx) {
        // En el constructor directament obro la comunicació amb la base de dades
        this.dbHelper = new DBHelper(ctx);

        // amés també construeixo dos databases un per llegir i l'altre per alterar
        this.open();
    }

    // DESTRUCTOR
    protected void finalize () {
        // Cerramos los databases
        this.dbW.close();
        this.dbR.close();
    }

    private void open() {
        this.dbW = this.dbHelper.getWritableDatabase();
        this.dbR = this.dbHelper.getReadableDatabase();
    }

    // Read

    public Cursor getMachines() {
        return dbR.rawQuery("select * from machines", null);
    }

    public Cursor getMachinesByZone(int id) {
        return dbR.rawQuery("select * from machines where id_zone=" + id, null);
    }

    public Cursor getMachine(int id) {
        return dbR.rawQuery("select * from machines where _id=" + (id), null);
    }

    public boolean serialNumberExists(String serialNumber) {
        Cursor c = this.getMachines();
        c.moveToFirst();

        for(int i = 0; i < c.getCount(); i++) {
            String other_serial = c.getString(c.getColumnIndexOrThrow("serial_number"));
            Log.d("asdf", serialNumber + " = " + other_serial);
            if(serialNumber.equals(other_serial)) return true;
            c.moveToNext();
        }
        return false;
    }

    public Cursor getZones() {
        return dbR.rawQuery("select * from zones", null);
    }

    public Cursor getZone(int id) {
        return dbR.rawQuery("select * from zones where _id=" + (id), null);
    }

    public Cursor getTypes() {
        return dbR.rawQuery("select * from types", null);
    }

    public Cursor getType(int id) {
        return dbR.rawQuery("select * from types where _id=" + (id), null);
    }

    public Cursor getTypeNames() {
        Cursor c = dbR.rawQuery("select _id, name from types", null);
        c.moveToFirst();
        return c;
    }

    public Cursor getZoneNames() {
        Cursor c = dbR.rawQuery("select _id, name from zones", null);
        c.moveToFirst();
        return c;
    }

    // Create

    public long insertMachine(ContentValues values) {
        return dbR.insert("machines", null, values);
    }

    public long insertZone(ContentValues values) {
        return dbR.insert("zones", null, values);
    }

    public long insertType(ContentValues values) {
        return dbR.insert("types", null, values);
    }

    // Update

    public void updateMachine(int id, ContentValues values) {
        dbW.update("machines", values, "_id=?", new String[] { String.valueOf(id) });
    }

    public void updateZone(int id, ContentValues values) {
        dbW.update("zones", values, "_id=?", new String[] { String.valueOf(id) });
    }

    public void updateType(int id, ContentValues values) {
        dbW.update("types", values, "_id=?", new String[] { String.valueOf(id) });
    }

    // Delete

    public void deleteMachine(int id) {
        dbW.delete("machines", "_id=?", new String[] { String.valueOf(id) });
    }

    public boolean deleteZone(int id) {
        Cursor c = dbR.rawQuery("select name from machines where id_zone=" + id, null);
        c.moveToFirst();
        if(c.getCount() > 0) return false;
        dbW.delete("zones", "_id=?", new String[] { String.valueOf(id) });
        return true;
    }

    public boolean deleteType(int id) {
        Cursor c = dbR.rawQuery("select * from machines where id_type=" + id, null);
        c.moveToFirst();
        Log.d("asdf", id + ": " + c.getCount());
        if(c.getCount() > 0) return false;
        dbW.delete("types", "_id=?", new String[] { String.valueOf(id) });
        return true;
    }
}
