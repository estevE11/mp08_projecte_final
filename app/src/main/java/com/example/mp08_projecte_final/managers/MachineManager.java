package com.example.mp08_projecte_final.managers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.db.DBDatasource;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MachineManager extends AppCompatActivity {

    private DBDatasource db;

    private boolean editMode = false;
    private int machineId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);
        this.loadSpinners();

        Bundle b = this.getIntent().getExtras();
        this.editMode = b.getBoolean("editMode");

        if(this.editMode) {
            this.machineId = b.getInt("id");
            this.loadData();
        }

        findViewById(R.id.btn_machine_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        findViewById(R.id.input_date).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final EditText dateInput = findViewById(R.id.input_date);
                CalendarUtils.openDateInput(MachineManager.this, dateInput);
            }
        });
    }

    public void loadSpinners() {
        Cursor type_names = this.db.getTypeNames();
        Cursor zone_names = this.db.getZoneNames();
        type_names.moveToFirst();
        zone_names.moveToFirst();

        String[] queryCols=new String[]{"_id", "name"};
        String[] adapterCols=new String[]{"name"};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        CursorAdapter typeArrayAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, type_names, adapterCols, adapterRowViews,0);
        CursorAdapter zoneArrayAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, zone_names, adapterCols, adapterRowViews,0);


        @SuppressLint("WrongViewCast") Spinner sp_types = (Spinner)findViewById(R.id.spinner_type);
        @SuppressLint("WrongViewCast") Spinner sp_zones = (Spinner)findViewById(R.id.spinner_zone);

        sp_types.setAdapter(typeArrayAdapter);
        sp_zones.setAdapter(zoneArrayAdapter);
    }

    private void loadData() {
        TextView input_name = (TextView)findViewById(R.id.input_machine_name);
        TextView input_serial = (TextView)findViewById(R.id.input_serial_number);
        TextView input_address = (TextView)findViewById(R.id.input_address);
        TextView input_zip = (TextView)findViewById(R.id.input_zip_code);
        TextView input_telf = (TextView)findViewById(R.id.input_telf);
        TextView input_city = (TextView)findViewById(R.id.input_city);
        TextView input_client = (TextView)findViewById(R.id.input_client_name);
        TextView input_email = (TextView)findViewById(R.id.input_email);
        TextView input_last_check = (TextView)findViewById(R.id.input_date);
        @SuppressLint("WrongViewCast") Spinner sp_types = (Spinner)findViewById(R.id.spinner_type);
        @SuppressLint("WrongViewCast") Spinner sp_zones = (Spinner)findViewById(R.id.spinner_zone);

        Cursor c = this.db.getMachine(this.machineId);
        c.moveToFirst();

        input_name.setText(c.getString(c.getColumnIndex("name")));
        input_serial.setText(c.getString(c.getColumnIndex("serial_number")));
        input_address.setText(c.getString(c.getColumnIndex("address")));
        input_zip.setText(c.getString(c.getColumnIndex("zip_code")));
        input_telf.setText(c.getString(c.getColumnIndex("telf")));
        input_city.setText(c.getString(c.getColumnIndex("city")));
        input_client.setText(c.getString(c.getColumnIndex("client_name")));
        input_email.setText(c.getString(c.getColumnIndex("email")));
        input_last_check.setText(c.getString(c.getColumnIndex("last_check")));


        // Set spinners
        /*
        Cursor type = (Cursor)sp_types.getSelectedItem();
        Cursor zone = (Cursor)sp_zones.getSelectedItem();
        int id_type = type.getInt(type.getColumnIndex("_id"));
        int id_zone = zone.getInt(type.getColumnIndex("_id"));
        */

        int id_type = c.getInt(c.getColumnIndex("id_type"));
        int id_zone = c.getInt(c.getColumnIndex("id_zone"));


        for(int i = 0; i < sp_types.getAdapter().getCount(); i++) {
            int curr_id = ((Cursor)sp_types.getItemAtPosition(i)).getInt(0);
            if(curr_id == id_type) {
                sp_types.setSelection(i);
            }
        }

        for(int i = 0; i < sp_zones.getAdapter().getCount(); i++) {
            int curr_id = ((Cursor)sp_zones.getItemAtPosition(i)).getInt(0);
            if(curr_id == id_type) {
                sp_zones.setSelection(i);
            }
        }
    }

    public void submit() {
        boolean insert = true;

        TextView input_name = (TextView)findViewById(R.id.input_machine_name);
        TextView input_serial = (TextView)findViewById(R.id.input_serial_number);
        TextView input_address = (TextView)findViewById(R.id.input_address);
        TextView input_zip = (TextView)findViewById(R.id.input_zip_code);
        TextView input_telf = (TextView)findViewById(R.id.input_telf);
        TextView input_city = (TextView)findViewById(R.id.input_city);
        TextView input_client = (TextView)findViewById(R.id.input_client_name);
        TextView input_email = (TextView)findViewById(R.id.input_email);
        TextView input_last_check = (TextView)findViewById(R.id.input_date);
        @SuppressLint("WrongViewCast") Spinner sp_types = (Spinner)findViewById(R.id.spinner_type);
        @SuppressLint("WrongViewCast") Spinner sp_zones = (Spinner)findViewById(R.id.spinner_zone);

        String name = input_name.getText().toString();
        String serial = input_serial.getText().toString();
        String address = input_address.getText().toString();
        String zip = input_zip.getText().toString();
        String telf = input_telf.getText().toString();
        String city = input_city.getText().toString();
        String client = input_client.getText().toString();
        String email = input_email.getText().toString();
        String last_check = input_last_check.getText().toString();
        Cursor type = (Cursor)sp_types.getSelectedItem();
        Cursor zone = (Cursor)sp_zones.getSelectedItem();
        int id_type = type.getInt(type.getColumnIndex("_id"));
        int id_zone = zone.getInt(type.getColumnIndex("_id"));

        String err_required = "This field is required";

        if(name.isEmpty()) {
            input_name.setError(err_required);
            insert = false;
        }
        if(serial.isEmpty()){
            input_serial.setError(err_required);
            insert = false;
        }
        if(address.isEmpty()){
            input_address.setError(err_required);
            insert = false;
        }
        if(zip.isEmpty()){
            input_zip.setError(err_required);
            insert = false;
        }
        if(city.isEmpty()){
            input_city.setError(err_required);
            insert = false;
        }
        if(client.isEmpty()){
            input_client.setError(err_required);
            insert = false;
        }

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(!email.matches(regex)) {
            input_email.setError("Email not valid");
            insert = false;
        }

        if(this.db.serialNumberExists(serial)) {
            input_serial.setError("Serial number already exists!");
            insert = false;
        }

        //TODO: Check telf is correct

        if(!insert) return;
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("serial_number", serial);
        values.put("address", address);
        values.put("zip_code", zip);
        values.put("telf", telf);
        values.put("city", city);
        values.put("email", email);
        values.put("client_name", client);
        values.put("last_check", last_check);
        values.put("id_zone", id_zone);
        values.put("id_type", id_type);

        if(!this.editMode) this.db.insertMachine(values);
        else this.db.updateMachine(this.machineId, values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle b = new Bundle();

        b.putInt("tab", 0);

        intent.putExtras(b);
        startActivity(intent);
    }
}