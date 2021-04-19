package com.example.mp08_projecte_final.managers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.db.DBDatasource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;

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
    }

    public void loadSpinners() {
        ArrayList<String> type_names = this.db.getTypeNames();
        ArrayList<String> zone_names = this.db.getZoneNames();

        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_names);
        ArrayAdapter<String> zoneArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zone_names);

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
        sp_types.setSelection(c.getInt(c.getColumnIndex("id_type")));
        sp_zones.setSelection(c.getInt(c.getColumnIndex("id_zone")));
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
        int id_type = sp_types.getSelectedItemPosition();
        int id_zone = sp_zones.getSelectedItemPosition();

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

        //TODO: Check if email is correct
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(!email.matches(regex)) {
            input_email.setError("Email not valid");
            insert = false;
        }

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