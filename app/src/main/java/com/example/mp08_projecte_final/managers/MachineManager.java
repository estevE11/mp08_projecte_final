package com.example.mp08_projecte_final.managers;

import android.os.Bundle;

import com.example.mp08_projecte_final.db.DBDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.mp08_projecte_final.R;

public class MachineManager extends AppCompatActivity {

    private DBDatasource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);
    }
}