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

public class TypeManager extends AppCompatActivity {

    private DBDatasource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);

        findViewById(R.id.button_type_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        String name = ((TextView)findViewById(R.id.input_type_name)).getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        this.db.insertType(values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}