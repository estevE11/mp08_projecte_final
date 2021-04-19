package com.example.mp08_projecte_final.managers;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.mp08_projecte_final.db.DBDatasource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;

import java.util.ArrayList;

public class MachineManager extends AppCompatActivity {

    private DBDatasource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);
        this.loadSpinners();

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
        String address = input_serial.getText().toString();

    }
}