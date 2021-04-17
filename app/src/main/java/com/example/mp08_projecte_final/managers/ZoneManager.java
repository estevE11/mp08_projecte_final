package com.example.mp08_projecte_final.managers;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;

public class ZoneManager extends AppCompatActivity {

    private DBDatasource db;

    private int zoneId = 0;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);

        Bundle b = this.getIntent().getExtras();
        this.editMode = b.getBoolean("editMode");

        if(this.editMode) {
            this.zoneId = b.getInt("id");
            this.loadData();
        }

        findViewById(R.id.button_zone_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void loadData() {
        Cursor type_data = this.db.getZone(this.zoneId);
        type_data.moveToFirst();
        String name = type_data.getString(1);

        ((TextView)findViewById(R.id.input_zone_name)).setText(name);
    }

    public void submit() {
        String name = ((TextView)findViewById(R.id.input_zone_name)).getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("lat", 12.0);
        values.put("lon", 12);
        values.put("description", "buenas");
        if(!this.editMode) this.db.insertZone(values);
        else this.db.updateZone(this.zoneId, values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle b = new Bundle();

        b.putInt("tab", 2);

        intent.putExtras(b);
        startActivity(intent);
    }
}