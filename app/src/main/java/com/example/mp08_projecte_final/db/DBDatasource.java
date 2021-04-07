package com.example.mp08_projecte_final.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public Cursor getMachine(int id) {
        return dbR.rawQuery("select * from machines where _id=" + (id), null);
    }

    public Cursor getZones() {
        return dbR.rawQuery("select * from zones", null);
    }

    public Cursor getZone(int id) {
        return dbR.rawQuery("select * from machines where _id=" + (id), null);
    }

    public Cursor getTypes() {
        return dbR.rawQuery("select * from types", null);
    }

    public Cursor getType(int id) {
        return dbR.rawQuery("select * from machines where _id=" + (id), null);
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

    public void deleteZones(int id) {
        dbW.delete("zones", "_id=?", new String[] { String.valueOf(id) });
    }

    public void deleteTypes(int id) {
        dbW.delete("types", "_id=?", new String[] { String.valueOf(id) });
    }
}
