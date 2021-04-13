package com.example.mp08_projecte_final.managers;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;

public class ZoneManager extends AppCompatActivity {

    private DBDatasource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);

        findViewById(R.id.button_zone_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        String name = ((TextView)findViewById(R.id.input_zone_name)).getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("latitude", 12.0);
        values.put("longitude", 12);
        values.put("description", "buenas");
        this.db.insertZone(values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}